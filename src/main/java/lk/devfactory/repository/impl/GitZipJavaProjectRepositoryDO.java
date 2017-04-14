package lk.devfactory.repository.impl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.zeroturnaround.exec.InvalidExitValueException;

import lk.devfactory.ds.RepositoryDS;
import lk.devfactory.exception.RepositoryNotFoundException;
import lk.devfactory.model.DeadCode;
import lk.devfactory.model.Repository;
import lk.devfactory.repository.RepositoryDO;
import lk.devfactory.store.impl.UUID;

//TODO Add javadocs.Fix the exception throwing.
@org.springframework.stereotype.Repository
public class GitZipJavaProjectRepositoryDO implements RepositoryDO {
	
	static private final Logger log = LoggerFactory.getLogger(GitZipJavaProjectRepositoryDO.class);
	
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
	public Repository add(UUID id, final Repository repository) {
		
		if (!StringUtils.isNotEmpty(repository.getUrl())) {
			throw new IllegalArgumentException("Url should not be empty");
		}
		
		final String repoId = id.toString();
		repository.setId(repoId);
		repository.setUrl(repository.getUrl());
		repository.setStatus("pending");
		log.info("Change reposioty status " + repository);

		//Caution The repository object is not thread safe because it is shared between thread from the cache
		boolean existing = !repositoryDS.create(id, repository); 

		if (!existing) {
			Runnable task = () -> { //TODO Change this to a thread pool
			    String threadName = Thread.currentThread().getName();
			    log.debug("Task running on thread " + threadName + " for repository "+repository);
				try {
					repository.setStatus("preparing");
					log.info("Change reposioty status " + repository);
		
					pd.download(repository.getUrl(), repoId);
	
					ep.extract(repoId);
					pj.renameProjectFile(repository.getUrl(), repoId);
					
					log.info("Change reposioty status " + repository);
					
					sp.executeUnd(repoId);
					sp.executeUndAnalyse(repoId);
					repository.setPreparedOn(OffsetDateTime.now());
					
					repository.setStatus("analysing");
					List<DeadCode> deadCodeList =sp.executeDeeadCodeJar(repoId);
		
					repository.setDeadCode(deadCodeList);
					repositoryDS.update(id, repository);
					repository.setCompletedOn(OffsetDateTime.now());
					repository.setStatus("completed");
					log.info("Change reposioty status " + repository);
				} catch (RuntimeException re) {
				    updateMessage(re, repository);
					repository.setStatus("failed"); //TODO return the full error detail
					log.info("Change reposioty status " + repository);
					throw re;
				}
			};

			Thread thread = new Thread(task);
			thread.start();
			log.debug("Started thread for repository "+repository);
		} else {
			return repositoryDS.findByNonIdUniqueKey(repository.getUrl());
		}
		return repository;
	}

	private void updateMessage(RuntimeException re, Repository repository) {
      if (re.getCause() == null) {
        repository.setMessage("System error please contact administrator.");
      } else if (re.getCause() instanceof RepositoryNotFoundException) {
        repository.setMessage("Repository not found at GitHub.");
      } else if (re.getCause() instanceof InvalidExitValueException) {
        repository.setMessage("Repository added could not be processed due to limitations in the application."
            + "The repository should be a java project with standard maven project structure.");
      } else {
        repository.setMessage("System error please contact administrator.");
      }
    
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
