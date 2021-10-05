import logo from './logo.svg';
import './App.css';
import Header from "./components/Header";
import Board from "./components/Board";
import ToDoData from "./components/ToDos.json"
import {useEffect, useState} from "react";
import getTodos from "./service/todo-api-service";

function App() {
    const [toDoState, setToDoState] = useState([])
    const [description, setDescription] = useState("")


    useEffect(() => {
        getTodos()
            .then(todos => setToDoState(todos))
    }, [])

    function fetchTodos() {
        fetch("/api/todo")
            .then(response => setToDoState(response.data))
            .catch(console.error)
    }

    const nextStatus = {
        OPEN: 'IN_PROGRESS',
        IN_PROGRESS: 'DONE',
    }

    function updateTodo(todo) {
        const advancedTodo = {...todo, status: nextStatus[todo.status]}
        fetch('/api/todo/' + advancedTodo.id, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(advancedTodo),
        })
            .then(response => response.json())
            .then(todo => {
                //console.log('Success:', todo);
                //console.log("Advanced", advancedTodo)
                const newTodos = toDoState.filter(todo => todo.id !== advancedTodo.id)
                // console.log("newTodo", ...newTodos)
                setToDoState([...newTodos, advancedTodo])
                //console.log("Status setzen", toDoState)
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }

    function deleteTodo(todo) {
        fetch('/api/todo/' + todo.id, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(todo)

        })
            .then(() => setToDoState(toDoState.filter(item => item.id !== todo.id)))
            .catch((error) => {
                console.error('Error:', error);
            });
    }


    const handleDescriptionChange = event => setDescription(event.target.value)

    const handleSubmit = event => {
        event.preventDefault()
        addTodo(description)
        setDescription('')
    }

    const addTodo = description => {
        const todo = {description, status: 'OPEN'}
        fetch('/api/todo', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(todo),
        })
            .then(response => response.json())
            .then(todo => {
                //console.log('Success:', todo);
                setToDoState([...toDoState, todo])
            })
            .catch((error) => {
                console.error('Error:', error);
            });
    }


    return (
        <div>
            <Header title="ToDo App"/>
            <Board toDos={toDoState} updateTodo={updateTodo} deleteTodo={deleteTodo}/>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="new-todo"
                    placeholder="Describe the new todo"
                    value={description}
                    onChange={handleDescriptionChange}
                />
                <button type="submit">Add</button>
            </form>
        </div>

    );
}

export default App;
