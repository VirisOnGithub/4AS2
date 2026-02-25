dist = [
    [0, 2, 1, 2, 3],
    [2, 0, 3, 2, 1],
    [1, 3, 0, 1, 2],
    [2, 2, 1, 0, 1],
    [3, 1, 2, 1, 0]
]

nc = [
    [0, 12, 4, 3, 7],
    [12, 0, 0, 6, 1],
    [4, 0, 0, 0, 2],
    [3, 6, 0, 0, 0],
    [7, 1, 2, 0, 0]
]

# x [1, 2, 3, 4, 5]
def f(x: list[int]) -> int:
    total = 0
    for i in range(5):
        for j in range(i + 1, 5):
            # print("i = ", i, "j = ", j, "   ", nc[x[i]-1][x[j]-1], "*", dist[i][j])
            total += nc[x[i]-1][x[j]-1] * dist[i][j]
    return total

print(f([1, 3, 4, 5, 2]))
print(f([4, 3, 1, 5, 2]))
print(f([1, 3, 5, 4, 2]))
print(f([1, 3, 4, 2, 5]))
print(f([1, 2, 4, 5, 3]))