package lk.devfactory.repository.impl;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.models.DeadCode;
import lk.devfactory.models.Repository;
import lk.devfactory.repository.RepositoryDO;
import lk.devfactory.store.impl.UUID;

//TODO Logger setting.Fix the exception throwing. Fix the prjPath.remove DS setter.
@org.springframework.stereotype.Repository
public class GitZipJavaProjectRepositoryDO implements RepositoryDO {
	
	@Qualifier("repositoryCacheDS")
	@Autowired
	public RepositoryDS<UUID, Repository, String> repositoryDS;
	
	@Autowired
	public SystemProcess sp;
	
	@Autowired
	public ProjectDirectory pj;
	
	@Autowired
	ExtractProject ep;
	
	@Autowired
	ProjectDownload pd;

	@Override
	//TODO replace Repository.getID to UUID type. Then remove this additional UUID parameter
	public Repository add(UUID id, Repository repository) {
		repository.setStatus("pending");
		boolean existing = !repositoryDS.create(id, repository);
		//TODO Cannot add already existing originalRepo with this check.
		//TODO need to make following block of code in if condition concurrent
		if (!existing) {
			repository.setStatus("downloading");
			String repoId = id.toString();
			pd.download(repository.getUrl(), repoId);
			//TODO Handle no originalRepo found and propagate the exception back from here
			//TODO Change the default status list in swagger enum
			ep.extract(repoId);
			pj.renameProjectFile(repository.getUrl(), repoId);
			
			repository.setStatus("analysing");
			
			if (!sp.executeUnd(repoId)) {
				repositoryDS.remove(id);
				repository.setStatus("failed");
				return repository;
			}
			sp.executeUndAnalyse(repoId);
			
			List<DeadCode> deadCodeList =sp.executeDeeadCodeJar(repoId);

			repository.setDeadCode(deadCodeList);
			repositoryDS.update(id, repository);
			repository.setStatus("completed");
			repository.setId(repoId);
		} else {
			repository = repositoryDS.findByNonIdUniqueKey(repository.getUrl());
		}
		return repository;
	}

	@Override
	public Stream<Repository> findAll() {
		return repositoryDS.findAll();
	}

	@Override
	public Repository findById(UUID id) {
		return repositoryDS.findById(id);
	}
	
	@Override
	public void remove(UUID id) {
		repositoryDS.remove(id);
	}
}
