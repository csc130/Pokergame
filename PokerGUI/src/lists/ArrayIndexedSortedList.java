package lists;

public class ArrayIndexedSortedList<T> extends ArraySortedList3<T>{
	
	public T get(int index){
		if(index < 0 || index >= list.length){
			throw new IndexOutOfBoundsException();
		}
		return list[index];
	}
	
	public T[] toArray(){
		return list;
	}
	
	public void clear(){
		list = null;
		list = (T[])new Object[5];
	}
}
