package hr.fer.zemris.ml.grouping.dataset;



import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import Jama.Matrix;

public class Dataset implements Iterable<Matrix> {
	private final List<Matrix> dataset;
	private final List<String> label;
	private final Set<String> labelSet;

	public Dataset(List<Matrix> data, List<String> l) {
		this.dataset = data;
		this.label = l;
		this.labelSet =  new HashSet<String>(l);
	}
	
	public Set<String> getLabelSet() {
		return labelSet;
	}
	
	public Matrix get(int i) {
		return dataset.get(i);
	}
	
	public String getLabel(int i) {
		return label.get(i);
	}

	@Override
	public DatasetIterator iterator() {
		return new DatasetIterator(this);
	}

	public int getSize() {
		return dataset.size();
	}
	
	public class DatasetIterator implements Iterator<Matrix> {
		private final Dataset dataset;
		public int idx;
		public int size;
		
		public DatasetIterator(Dataset dataset) {
			this.dataset = dataset;
			this.idx = 0;
			this.size = this.dataset.dataset.size();
		}

		@Override
		public boolean hasNext() {
			if(idx < size) {
				return true;
			} else {
				return false;
			}
		}

		@Override
		public Matrix next() {
			int current = idx;
			++idx;
			return dataset.dataset.get(current);
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
