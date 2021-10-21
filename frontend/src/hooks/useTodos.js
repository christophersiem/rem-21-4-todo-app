import { useEffect, useState } from 'react'
import {
  deleteTodo,
  getTodos,
  postTodo,
  putTodo,
} from '../service/todo-api-service'
import { getNextStatus } from '../service/todo-service'

export default function useTodos(token) {
  const [todos, setTodos] = useState([])

  const addTodo = description => {
    postTodo(description, token).then(addedTodo => setTodos([...todos, addedTodo]))
  }

  const advanceTodo = todo => {
    const newStatus = getNextStatus(todo.status)
    const advancedTodo = { ...todo, status: newStatus }
    putTodo(advancedTodo, token).then(updatedTodo =>
      setTodos(
        todos.map(item => (updatedTodo.id === item.id ? advancedTodo : item))
      )
    )
  }

  const removeTodo = id => {
    deleteTodo(id, token).then(() => setTodos(todos.filter(todo => todo.id !== id)))
  }

  useEffect(() => {
    getTodos(token)
      .then(todos => setTodos(todos))
      .catch(error => console.error(error.message))
  }, [token])

  return { todos, addTodo, advanceTodo, removeTodo }
}
