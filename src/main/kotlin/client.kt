import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.dom.clear
import org.w3c.dom.*

data class Model(val elems: List<String>, val interval: Int?)

sealed class Action {
    data class ButtonClicked(val value: String) : Action()
    object StartTimer : Action()
    object TimerTriggered : Action()
}

fun renderCount(elems: List<String>): HTMLElement {
    val wrapper = document.createElement("div") as HTMLDivElement
    wrapper.innerText = "Count: ${elems.size}"
    return wrapper
}

fun renderList(elems: List<String>): HTMLElement {
    val list = document.createElement("ul") as HTMLUListElement

    for (elem in elems) {
        val entry = document.createElement("li") as HTMLLIElement
        entry.innerText = "Element: $elem"
        list.appendChild(entry)
    }

    return list
}

fun renderInput(dispatch: (Action) -> Unit): HTMLElement {
    val form = document.createElement("form") as HTMLFormElement
    val input = document.createElement("input") as HTMLInputElement
    input.type = "text"
    form.appendChild(input)

    val button = document.createElement("button") as HTMLButtonElement
    button.type = "submit"
    button.innerText = "Add"
    form.appendChild(button)

    val timer = document.createElement("button") as HTMLButtonElement
    timer.type = "button"
    timer.innerText = "Timer"
    timer.addEventListener("click", {
        dispatch(Action.StartTimer)
    })
    form.appendChild(timer)


    form.addEventListener("submit", {
        val value = input.value.trim()
        if (value.isNotEmpty()) {
            dispatch(Action.ButtonClicked(value))
        }
    })

    return form
}

fun render(model: Model, dispatch: (Action) -> Unit): HTMLElement {
    val container = document.createElement("div") as HTMLDivElement

    container.appendChild(renderCount(model.elems))
    container.appendChild(renderList(model.elems))
    container.appendChild(renderInput(dispatch))

    return container
}

fun dispatch(model: Model, action: Action): Model = when(action) {
    is Action.ButtonClicked -> {
        model.copy( elems = model.elems + action.value)
    }
    Action.StartTimer -> {
        if (model.interval == null) {
            model.copy(interval = window.setInterval({
                println("test")
            }, 1000))
        } else {
            window.clearInterval(model.interval)
            model.copy(interval = null)
        }
    }
    Action.TimerTriggered -> TODO()
}

fun <M, V, A> runApp(model: M, render: (M, (A) -> Unit) -> V, dispatcher: (M, A) -> M, presenter: (V) -> Unit) {
    val view: V = render(model) { a ->
        runApp(dispatcher(model, a), render, dispatcher, presenter)
    }

    presenter(view)
}

fun <M, V : HTMLElement, A> runHtmlApp(model: M, render: (M, (A) -> Unit) -> V, dispatcher: (M, A) -> M) {
    runApp(model, render, dispatcher) { view ->
        document.body?.let {
            it.clear()
            it.append(view)
        }
    }
}

fun main() {
    window.onload = { runHtmlApp(Model(emptyList(), null), ::render, ::dispatch) }
}
