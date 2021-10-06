import styled from "styled-components/macro";
import Board from "./Board";
import {useLocation, useParams} from "react-router-dom";

export default function BoardsOverviewPath({todos, onAdvance, onDelete}) {

    const openTodos = todos.filter(todo => todo.status === "OPEN")
    const inProgressTodos = todos.filter(todo => todo.status === "IN_PROGRESS")
    const doneTodos = todos.filter(todo => todo.status === "DONE")
    const { todoStatus } = useParams()

    function useQuery() {
        return new URLSearchParams(useLocation().search);
    }
    const query = useQuery();
    const color = query.get("color");
    console.log(color);

    return (
        <Main>
            {todoStatus === "open" && <Board title="Open"
                                             todos={openTodos}
                                             onAdvance={onAdvance}/>}
            {todoStatus === "inprogress" && <Board title= {`In Progress + ${color}`}
                                                   todos={inProgressTodos}
                                                   onAdvance={onAdvance}/>}
            {todoStatus === "done" && <Board title="Done"
                                             todos={doneTodos}
                                             onDelete={onDelete}
            />}
        </Main>
    )
}

const Main = styled.main`
  overflow-y: scroll;
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  justify-items: center;
`