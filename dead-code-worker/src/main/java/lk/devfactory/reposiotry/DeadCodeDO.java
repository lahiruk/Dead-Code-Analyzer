package lk.devfactory.reposiotry;

import java.util.List;

import lk.devfactory.model.DeadCode;

public interface DeadCodeDO {
	public List<DeadCode> analyse(String repoId);
}
