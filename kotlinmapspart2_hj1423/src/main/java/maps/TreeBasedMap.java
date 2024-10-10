package maps;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TreeBasedMap<K, V> implements CustomMutableMap<K, V> {
    private final Comparator<K> keyComparator;
    private TreeMapNode<K, V> root;

    public TreeBasedMap(Comparator<K> keyComparator) {
        this.keyComparator = keyComparator;
        this.root = null;
    }

    @NotNull
    @Override
    public Iterable<Entry<K, V>> getEntries() {
        return EntriesIterator::new;
    }

    private class EntriesIterator implements Iterator<Entry<K, V>> {
        private final Deque<TreeMapNode<K, V>> stack;

        private EntriesIterator() {
            this.stack = new LinkedList<>();
            fillStack(root); // Fill the stack with nodes for in-order traversal
        }

        private void fillStack(TreeMapNode<K, V> node) {
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            TreeMapNode<K, V> current = stack.pop() ;
            fillStack(current.getRight()); // Fill the stack with right subtree for in-order traversal
            return new Entry<>(current.getKey(), current.getValue());
        }
    }

    @NotNull
    @Override
    public Iterable<K> getKeys() {
        return KeysIterator::new;
    }

//    private class KeysIterable implements Iterable<K> {
//        @NotNull
//        @Override
//        public Iterator<K> iterator() {
//            return new KeysIterator();
//        }
//    }

    private class KeysIterator implements Iterator<K> {
        private final Iterator<Entry<K, V>> entriesIterator;

        public KeysIterator() {
            this.entriesIterator = new EntriesIterator();
        }

        @Override
        public boolean hasNext() {
            return entriesIterator.hasNext();
        }

        @Override
        public K next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return entriesIterator.next().getKey();
        }
    }

    @NotNull
    @Override
    public Iterable<V> getValues() {
        return () -> new ValuesIterator();
    }


    private class ValuesIterator implements Iterator<V> {
        private final Iterator<Entry<K, V>> entriesIterator;

        public ValuesIterator() {
            this.entriesIterator = new EntriesIterator();
        }

        @Override
        public boolean hasNext() {
            return entriesIterator.hasNext();
        }

        @Override
        public V next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return entriesIterator.next().getValue();
        }
    }

    @Nullable
    @Override
    public V get(K key) {
        return search(root, key);
    }

    private V search(TreeMapNode<K, V> node, K key) {
        if (node == null) return null;
        int cmp = keyComparator.compare(key, node.getKey());
        if (cmp == 0) return node.getValue(); // Found key, return value
        else if (cmp < 0) return search(node.getLeft(), key); // Search left subtree
        else return search(node.getRight(), key); // Search right subtree
    }

    private V insert(TreeMapNode<K, V> node, K key, V value) {
        if (node == null) {
            root = new TreeMapNode<>(key, value);
            return null; // No previous value, inserted as root
        }
        int cmp = keyComparator.compare(key, node.getKey());
        if (cmp == 0) {
            V oldValue = node.getValue();
            node.setValue(value); // Update value if key exists
            return oldValue;
        } else if (cmp < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new TreeMapNode<>(key, value));
                return null; // No previous value
            } else return insert(node.getLeft(), key, value); // Recur to left subtree
        } else {
            if (node.getRight() == null) {
                node.setRight(new TreeMapNode<>(key, value));
                return null; // No previous value
            } else return insert(node.getRight(), key, value); // Recur to right subtree
        }
    }

    @Nullable
    @Override
    public V set(K key, V value) {
        return insert(root, key, value);
    }

    @Nullable
    @Override
    public V put(K key, V value) {
        return insert(root, key, value);
    }

    @Nullable
    @Override
    public V put(Entry<K, V> entry) {
        return insert(root, entry.getKey(), entry.getValue());
    }

    @Nullable
    @Override
    public V remove(K key) {
        return removeHelper(root, null, key);
    }

    private V removeHelper(TreeMapNode<K, V> current, TreeMapNode<K, V> parent, K key) {
        if (current == null) return null;  // no key found
        int cmp = keyComparator.compare(key, current.getKey());
        if (cmp == 0) {  // found the matching key
            V prevVal = current.getValue();
            if (current.getRight() == null || current.getLeft() == null) deleteNode(current, parent);
            else {  // entry of current need to be replaced by smallest value in right subtree.
                TreeMapNode<K, V> toBeOverwritten = current;
                parent = current;
                current = current.getRight();
                while (current.getLeft() != null) {
                    parent = current;
                    current = current.getLeft();
                }
                toBeOverwritten.setKey(current.getKey());
                toBeOverwritten.setValue(current.getValue());
                deleteNode(current, parent);
            }
            return prevVal;  // return previous value associated with the key
        }
        else if (cmp < 0) return removeHelper(current.getLeft(), current, key);
        else return removeHelper(current.getRight(), current, key);
    }

    private void deleteNode(TreeMapNode<K, V> current, TreeMapNode<K, V> parent) {
        if (parent == null) {  // current node is root
            if (current.getLeft() == null) root = current.getRight();
            else root = current.getLeft();
        }
        else if (parent.getLeft() == current) {  // current node is left of the parent.
            parent.setLeft(current.getLeft() != null ? current.getLeft() : current.getRight());
        } else {  // current node is right subtree of the parent.
            parent.setRight(current.getLeft() != null ? current.getLeft() : current.getRight());
        }
    }

    @Override
    public boolean contains(K key) {
        return search(root, key) != null;
    }
}
