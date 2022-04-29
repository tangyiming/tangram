/**
 * 防抖函数
 * @param {*} func
 * @param {*} delay
 * @param {*} immediate 是否立即执行
 */
export function debounce(func, delay, immediate) {
    // 定时任务的id
    let timeout
    return function () {
        let that = this
        let args = arguments
        if (timeout) clearTimeout(timeout)
        if (immediate) {
            var callNow = !timeout
            timeout = setTimeout(() => {
                timeout = null
            }, delay)
            if (callNow) func.apply(that, args)
        } else {
            timeout = setTimeout(function () {
                func.apply(that, args)
            }, delay)
        }
    }
}

/*
 手机号校验
 */
export function checkTelephone(telephone) {
    if (/\d{11}$/.test(telephone)) {
        return true
    }
    return false
}

export const applyDrag = (arr, dragResult) => {
    const { removedIndex, addedIndex, payload } = dragResult
    if (removedIndex === null && addedIndex === null) return arr

    const result = [...arr]
    let itemToAdd = payload

    if (removedIndex !== null) {
        itemToAdd = result.splice(removedIndex, 1)[0]
    }

    if (addedIndex !== null) {
        result.splice(addedIndex, 0, itemToAdd)
    }
    return result
}
