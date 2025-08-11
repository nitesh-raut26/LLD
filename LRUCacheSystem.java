// =====================================================
// LRU CACHE SYSTEM - 
// =====================================================

import java.util.HashMap;
import java.util.Map;

// Main Class - Must be first and public
public class LRUCacheSystem {
    public static void main(String[] args) {
        System.out.println("=== LRU CACHE SYSTEM DEMO ===");
        System.out.println("=============================\n");
        
        // Demo 1: Basic operations
        System.out.println("1. Basic LRU Cache Operations:");
        LRUCache<String, String> cache = new LRUCache<>(3);
        
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.put("key3", "value3");
        
        System.out.println("\n2. Accessing key1 (should move to front):");
        cache.get("key1");
        
        System.out.println("\n3. Adding key4 (should evict key2):");
        cache.put("key4", "value4");
        
        System.out.println("\n4. Testing cache miss:");
        cache.get("key2"); // Should be cache miss
        
        System.out.println("\n5. Updating existing key:");
        cache.put("key1", "updated_value1");
        
        // Demo 2: Integer cache
        System.out.println("\n=== INTEGER CACHE DEMO ===");
        LRUCache<Integer, Integer> intCache = new LRUCache<>(2);
        
        intCache.put(1, 10);
        intCache.put(2, 20);
        intCache.put(3, 30); // Should evict key 1
        
        System.out.println("\n6. Testing integer cache:");
        intCache.get(1); // Cache miss
        intCache.get(2); // Cache hit
        
        // Demo 3: Cache operations
        System.out.println("\n=== CACHE OPERATIONS DEMO ===");
        LRUCache<String, String> operationsCache = new LRUCache<>(4);
        
        System.out.println("\n7. Testing cache operations:");
        operationsCache.put("A", "Apple");
        operationsCache.put("B", "Banana");
        operationsCache.put("C", "Cherry");
        
        System.out.println("Cache size: " + operationsCache.size());
        System.out.println("Cache capacity: " + operationsCache.getCapacity());
        System.out.println("Is empty: " + operationsCache.isEmpty());
        System.out.println("Is full: " + operationsCache.isFull());
        System.out.println("Contains key 'A': " + operationsCache.containsKey("A"));
        System.out.println("Contains key 'Z': " + operationsCache.containsKey("Z"));
        
        System.out.println("\n8. Removing a key:");
        operationsCache.remove("B");
        
        System.out.println("\n9. Clearing cache:");
        operationsCache.clear();
        
        // Demo 4: Performance test with String cache
        System.out.println("\n=== PERFORMANCE TEST ===");
        LRUCache<String, String> perfCache = new LRUCache<>(1000);
        
        System.out.println("\n10. Performance test with 1000 operations:");
        long startTime = System.nanoTime();
        
        // Add 500 items
        for (int i = 0; i < 500; i++) {
            perfCache.put("key" + i, "value" + i);
        }
        
        // Access some items (mix of hits and misses)
        for (int i = 0; i < 500; i++) {
            perfCache.get("key" + (i % 300)); // Some hits, some misses
        }
        
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0; // Convert to milliseconds
        
        System.out.println("Performance test completed in " + String.format("%.2f", duration) + " ms");
        System.out.println("Cache size: " + perfCache.size());
        perfCache.displayStatistics();
        
        System.out.println("\n=== LRU CACHE DEMO COMPLETED ===");
    }
}

// Generic LRU Cache Implementation
class LRUCache<K, V> {
    // Inner Node class for doubly linked list
    private class Node {
        K key;
        V value;
        Node prev;
        Node next;
        
        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public String toString() {
            return "(" + key + ":" + value + ")";
        }
    }
    
    private final int capacity;
    private final Map<K, Node> cache;
    private final Node head;
    private final Node tail;
    private int size;
    
    // Constructor
    public LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        
        this.capacity = capacity;
        this.cache = new HashMap<>();
        this.size = 0;
        
        // Create dummy head and tail nodes for easier manipulation
        this.head = new Node(null, null);
        this.tail = new Node(null, null);
        head.next = tail;
        tail.prev = head;
    }
    
    // Get value by key
    public V get(K key) {
        Node node = cache.get(key);
        if (node == null) {
            System.out.println("Cache MISS for key: " + key);
            return null;
        }
        
        System.out.println("Cache HIT for key: " + key + " -> " + node.value);
        // Move accessed node to head (mark as most recently used)
        moveToHead(node);
        displayCache();
        return node.value;
    }
    
    // Put key-value pair
    public void put(K key, V value) {
        Node existingNode = cache.get(key);
        
        if (existingNode != null) {
            // Update existing node
            System.out.println("Updating existing key: " + key + " with value: " + value);
            existingNode.value = value;
            moveToHead(existingNode);
        } else {
            // Add new node
            System.out.println("Adding new key: " + key + " with value: " + value);
            Node newNode = new Node(key, value);
            
            if (size >= capacity) {
                // Remove least recently used (tail node)
                Node tailNode = removeTail();
                cache.remove(tailNode.key);
                size--;
                System.out.println("Evicted LRU key: " + tailNode.key);
            }
            
            cache.put(key, newNode);
            addToHead(newNode);
            size++;
        }
        
        displayCache();
    }
    
    // Remove a key from cache
    public boolean remove(K key) {
        Node node = cache.get(key);
        if (node == null) {
            System.out.println("Key not found for removal: " + key);
            return false;
        }
        
        cache.remove(key);
        removeNode(node);
        size--;
        System.out.println("Removed key: " + key);
        displayCache();
        return true;
    }
    
    // Check if key exists
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
    
    // Get current size
    public int size() {
        return size;
    }
    
    // Get capacity
    public int getCapacity() {
        return capacity;
    }
    
    // Check if cache is empty
    public boolean isEmpty() {
        return size == 0;
    }
    
    // Check if cache is full
    public boolean isFull() {
        return size == capacity;
    }
    
    // Clear all entries
    public void clear() {
        cache.clear();
        head.next = tail;
        tail.prev = head;
        size = 0;
        System.out.println("Cache cleared");
        displayCache();
    }
    
    // Get all keys in LRU order (most recent first)
    public void printLRUOrder() {
        System.out.print("LRU Order (MRU -> LRU): ");
        Node current = head.next;
        while (current != tail) {
            System.out.print(current.key + " ");
            current = current.next;
        }
        System.out.println();
    }
    
    // Display current cache state
    public void displayCache() {
        if (size == 0) {
            System.out.println("Cache is empty");
            return;
        }
        
        System.out.print("Cache (MRU -> LRU): ");
        Node current = head.next;
        while (current != tail) {
            System.out.print(current + " ");
            current = current.next;
        }
        System.out.println(" | Size: " + size + "/" + capacity);
    }
    
    // Add node to head (most recently used position)
    private void addToHead(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    
    // Remove a node from the linked list
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    // Move node to head (mark as most recently used)
    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }
    
    // Remove tail node (least recently used)
    private Node removeTail() {
        Node lastNode = tail.prev;
        removeNode(lastNode);
        return lastNode;
    }
    
    // Get cache statistics
    public void displayStatistics() {
        System.out.println("\n=== Cache Statistics ===");
        System.out.println("Capacity: " + capacity);
        System.out.println("Current Size: " + size);
        System.out.println("Available Space: " + (capacity - size));
        System.out.println("Load Factor: " + String.format("%.2f", (double) size / capacity * 100) + "%");
        System.out.println("Is Empty: " + isEmpty());
        System.out.println("Is Full: " + isFull());
        
        if (!isEmpty()) {
            Node mru = head.next;
            Node lru = tail.prev;
            System.out.println("Most Recently Used: " + mru.key + " -> " + mru.value);
            System.out.println("Least Recently Used: " + lru.key + " -> " + lru.value);
        }
    }
}