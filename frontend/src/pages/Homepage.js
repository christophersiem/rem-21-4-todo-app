import BoardsOverview from '../components/BoardsOverview'
import NewTodo from '../components/NewTodo'
import PropTypes from 'prop-types'
import NavigationBar from "../components/NavigationBar";

Homepage.propTypes = {
  todos: PropTypes.array.isRequired,
  onAdvance: PropTypes.func.isRequired,
  onDelete: PropTypes.func.isRequired,
  onAdd: PropTypes.func.isRequired,
}

export default function Homepage({ todos, onAdvance, onDelete, onAdd }) {
  return (
    <>
        <NavigationBar />
      <BoardsOverview todos={todos} onAdvance={onAdvance} onDelete={onDelete} />
      <NewTodo onAdd={onAdd} />
    </>
  )
}
