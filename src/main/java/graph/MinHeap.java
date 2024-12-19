package graph;

/**
 * MinHeap class - Implements a min-heap priority queue specifically for Dijkstra's algorithm.
 * It stores nodes with their respective distances from the source.
 */
public class MinHeap {
    private MinHeapElement[] heap;   // Array representing the heap
    private int[] positions;   // Array to track the positions of nodes in the heap
    private int maxsize;   // Maximum size of the heap
    private int size;   // Current size of the heap

    /**
     * this class representing an element in the min-heap.
     */
    public class MinHeapElement{
       private int id;
       private int distance;

        /**
         * Constructor to initialize MinHeapElement with only an ID.
         * Distance is set to Integer.MIN_VALUE initially.
         * @param id - Node ID
         */
       public MinHeapElement(int id){
           this.id=id;
           this.distance=Integer.MIN_VALUE;
       }

        /**
         * Constructor to initialize MinHeapElement with an ID and a distance.
         * @param id - Node ID
         * @param distance - Distance from the source node
         */
        public MinHeapElement(int id, int distance) {
            this.id = id;
            this.distance = distance;
        }
        // Getter and setter methods for id and distance
        public int getId() {
            return id;
        }

        public int getDistance() {
            return distance;
        }

        public void setId(int id) {
            this.id = id;
        }

        public void setDistance(int distance) {
            this.distance = distance;
        }

    }

    public int getSize() {
        return size;
    }

    public int[] getPositions() {
        return positions;
    }
    /**
     * Constructor to initialize the MinHeap with a specified maximum size.
     * and initialize the first element with the min value
     * @param max - Maximum size of the heap
     */
    public MinHeap(int max) {
        maxsize = max;
        heap = new MinHeapElement[maxsize];
        positions=new int[maxsize];
        size = 0;
        heap[0] = new MinHeapElement(-1);
    }
    /**
     * Inserts a new node with a given priority (distance) into the heap.
     * and store the position of elements in the heap to the position array
     * @param nodeId - Node ID
     * @param priority - Priority (distance) of the node
     */
    public void insert(int nodeId,int priority) {
        if(size>=maxsize-1){
            throw new IllegalArgumentException();//heap is full
        }
        size++;
        heap[size] =new MinHeapElement(nodeId,priority) ;
        positions[nodeId]=size;
        int current = size;
        while (heap[current].distance < heap[parent(current)].distance) {
            swap(current, parent(current));
            current = parent(current);
        }
    }
    /**
     * Removes and returns the node with the smallest distance from the heap.
     * @return - Node ID of the element with the smallest distance
     */
    public int removeMin() {
        swap(1, size); // swap the end of the heap into the root
        size--;  	   // removed the end of the heap
        // fix the heap property - push down as needed
        if (size != 0)
            pushdown(1);
       return heap[size + 1].id;
    }
    /**
     * Pushes down the element at the given position to restore heap property.
     * @param position - Position to push down from
     */
    private void pushdown(int position) {
        int smallestchild;
        while (!isLeaf(position)) {
            smallestchild = leftChild(position); // set the index of the smallest child to left child
            if ((smallestchild < size) && (heap[smallestchild].distance > heap[smallestchild + 1].distance))
                smallestchild = smallestchild + 1; // right child was smaller, so smallest child = right child
            // the value of the smallest child is less than value of current,
            // the heap is already valid
            if (heap[position].distance <= heap[smallestchild].distance)
                return;
            swap(position, smallestchild);
            position = smallestchild;
        }
    }
    /**
     * Swaps two elements in the heap and updates their positions.
     * @param pos1 - Position of the first element
     * @param pos2 - Position of the second element
     */
    private void swap(int pos1, int pos2) {
        MinHeapElement tmp;
        //swapping places in the minHeap
        tmp = heap[pos1];
        heap[pos1] = heap[pos2];
        heap[pos2] = tmp;
        //swapping positions
        positions[heap[pos1].getId()] = pos1;
        positions[heap[pos2].getId()] = pos2;
    }
    private int parent(int pos) {
        return pos / 2;
    }
    private boolean isLeaf(int pos) {
        return ((pos > size / 2) && (pos <= size));
    }
    private int rightChild(int pos) {
        return 2 * pos + 1;
    }
    private int leftChild(int pos) {
        return 2 * pos;
    }
    public boolean empty() {
        return size==0;
    }
    /**
     * Reduces the priority (distance) of a given node and restores heap property.
     * @param nodeId - Node ID whose priority is to be reduced
     * @param newPriority - New priority (distance)
     */
    void reduceKey(int nodeId, int newPriority) {
        int index = positions[nodeId];
        heap[index].setDistance(newPriority);
        // Bubble up the element to restore heap property
        while (index > 1 && heap[index].distance < heap[parent(index)].distance) {
            swap(index, parent(index));
            index = parent(index);
        }
    }
}
