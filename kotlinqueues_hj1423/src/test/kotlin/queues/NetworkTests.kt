package queues

import kotlin.test.Test
import kotlin.test.assertEquals

class   NetworkTests {
    @Test
    fun `FIFO queue and sink combine OK`() {
        val sink: Sink<Int> = Sink()
        val fifoNode = QueueNode(FifoQueue(), sink)

        fifoNode.accept(1)
        fifoNode.accept(2)
        fifoNode.accept(3)
        fifoNode.forward()
        fifoNode.forward()
        fifoNode.forward()

        assertEquals(listOf(1, 2, 3), sink.getAccepted())
    }

    @Test
    fun `FIFO and LIFO queues combine OK`() {
        val sink: Sink<Int> = Sink()
        val lifoNode = QueueNode(LifoQueue(), sink)
        val fifoNode = QueueNode(FifoQueue(), lifoNode)

        fifoNode.accept(1)
        fifoNode.accept(2)
        fifoNode.forward()
        fifoNode.accept(3)
        fifoNode.forward()
        lifoNode.forward()
        fifoNode.forward()
        lifoNode.forward()
        lifoNode.forward()

        assertEquals(listOf(2, 3, 1), sink.getAccepted())
    }

    @Test
    fun `LIFO, Priority and FIFO queues combine OK`() {
        val sink: Sink<Int> = Sink()
        val lifoNode = QueueNode(LifoQueue(), sink)
        val prQueueNode = QueueNode(PrQueue(), lifoNode)
        val fifoNode = QueueNode(FifoQueue(), prQueueNode)

        fifoNode.accept(3)
        fifoNode.accept(1)
        fifoNode.forward()
        fifoNode.accept(2)
        fifoNode.accept(0)
        prQueueNode.forward()
        fifoNode.forward()
        fifoNode.forward()
        lifoNode.forward()
        fifoNode.forward()
        prQueueNode.forward()
        prQueueNode.forward()
        lifoNode.forward()
        lifoNode.forward()
        prQueueNode.forward()
        lifoNode.forward()

        assertEquals(listOf(3, 1, 0, 2), sink.getAccepted())
    }

    // TODO: add additional tests to check that queue networks are working.
}
