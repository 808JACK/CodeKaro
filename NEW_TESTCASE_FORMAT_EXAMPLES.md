# New Test Case Format Examples

## For "Delete Node in Linked List" Problem

**MinIO File: `Delete Node in Linked List.txt`**
```
4 5 1 9
5
→ output: 4 1 9

4 5 1 9
1
→ output: 4 5 9

1 2 3 4
3
→ output: 1 2 4

0 1
0
→ output: 1

1 2
1
→ output: 2
```

## For "Two Sum" Problem

**MinIO File: `Two Sum.txt`**
```
2 7 11 15
9
→ output: 0 1

3 2 4
6
→ output: 1 2

3 3
6
→ output: 0 1

1 2 3 4 5
8
→ output: 2 4
```

## User Code Example

**Delete Node in Linked List Solution:**
```python
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

def deleteNode(node):
    node.val = node.next.val
    node.next = node.next.next

def create_linked_list(arr):
    if not arr:
        return None
    head = ListNode(arr[0])
    curr = head
    for val in arr[1:]:
        curr.next = ListNode(val)
        curr = curr.next
    return head

def linked_list_to_array(head):
    result = []
    current = head
    while current:
        result.append(current.val)
        current = current.next
    return result

# Main solution
arr = list(map(int, input().split()))
node_val = int(input())

head = create_linked_list(arr)
curr = head
while curr and curr.val != node_val:
    curr = curr.next

if curr and curr.next:
    deleteNode(curr)

result = linked_list_to_array(head)
print(' '.join(map(str, result)))
```

**Two Sum Solution:**
```python
arr = list(map(int, input().split()))
target = int(input())

num_map = {}
for i, num in enumerate(arr):
    complement = target - num
    if complement in num_map:
        print(num_map[complement], i)
        break
    num_map[num] = i
```

## Benefits

✅ **No function name requirements**
✅ **No parameter signature matching** 
✅ **Users read input with `input()` and print output with `print()`**
✅ **Works with any problem type**
✅ **Easy to test locally**
✅ **Matches competitive programming style**

## Migration

1. **Update your MinIO files** to the new format shown above
2. **Restart Collab Service** to pick up the new parsing logic
3. **Test with the new user code style**

The backend now supports both formats during transition, so you can migrate gradually!