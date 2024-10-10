package tunes

import java.util.NoSuchElementException

class SongCollection {
    private class Song(val name: String, val tune: Tune)

    private class TreeNode(var song: Song, var left: TreeNode? = null, var right: TreeNode? = null)

    private var root: TreeNode? = null

    fun addSong(name: String, tune: Tune) {
        var current: TreeNode? = root
        if (current == null) {
            root = TreeNode(Song(name, tune))
            return
        }
        while (current!!.song.name != name) {
            if (current.song.name > name) {
                if (current.left != null) current = current.left
                else {
                    current.left = TreeNode(Song(name, tune))
                    return
                }
            } else {
                if (current.right != null) current = current.right
                else {
                    current.right = TreeNode(Song(name, tune))
                    return
                }
            }
        }
        throw UnsupportedOperationException()


    }

    fun getTune(name: String): Tune {
        var current: TreeNode? = root ?: throw NoSuchElementException()
        while (current!!.song.name != name) {
            if (current.song.name > name) {
                if (current.left != null) current = current.left
                else throw NoSuchElementException()
            } else {
                if (current.right != null) current = current.right
                else throw NoSuchElementException()
            }
        }
        return current.song.tune
    }

    private fun getSongNames(node: TreeNode?): List<String> =
        if (node == null) emptyList()
        else getSongNames(node.left) + node.song.name + getSongNames(node.right)

    fun getSongNames(): List<String> = getSongNames(root)
}
