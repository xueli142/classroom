import { onBeforeUnmount } from 'vue'

export const draggable = {
    mounted(el) {
        // 必须等 DOM 渲染完再查节点
        const header = el.querySelector('.el-dialog__header')
        const dialog = el.querySelector('.el-dialog')
        if (!header || !dialog) return

        let dragging = false
        let startX = 0
        let startY = 0
        let left = 0
        let top = 0

        header.style.cursor = 'move'

        const onMouseDown = (e) => {
            dragging = true
            startX = e.clientX
            startY = e.clientY
            const rect = dialog.getBoundingClientRect()
            left = rect.left
            top = rect.top
            document.addEventListener('mousemove', onMouseMove)
            document.addEventListener('mouseup', onMouseUp)
        }

        const onMouseMove = (e) => {
            if (!dragging) return
            const dx = e.clientX - startX
            const dy = e.clientY - startY
            dialog.style.position = 'absolute'
            dialog.style.left = `${left + dx}px`
            dialog.style.top = `${top + dy}px`
            dialog.style.marginLeft = '0'
            dialog.style.marginTop = '0'
        }

        const onMouseUp = () => {
            dragging = false
            document.removeEventListener('mousemove', onMouseMove)
            document.removeEventListener('mouseup', onMouseUp)
        }

        header.addEventListener('mousedown', onMouseDown)

        // 清理函数
        el._draggableDestroy = () => {
            header.removeEventListener('mousedown', onMouseDown)
            document.removeEventListener('mousemove', onMouseMove)
            document.removeEventListener('mouseup', onMouseUp)
        }
    },

    unmounted(el) {
        el._draggableDestroy && el._draggableDestroy()
    }
}