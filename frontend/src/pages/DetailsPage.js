import {useParams} from 'react-router-dom'
import {useEffect} from 'react'
import styled from 'styled-components/macro'
import useDetailedTodo from '../hooks/useDetailedTodo'
import NavigationBar from "../components/NavigationBar";

export default function DetailsPage() {
    const {id} = useParams()

    const {detailedTodo, getTodoById} = useDetailedTodo()

    useEffect(() => {
        getTodoById(id)
    }, [id, getTodoById])

    return (
        <>
            <NavigationBar/>
            <Wrapper>
                <h2>TODO</h2>
                <SubTitle>
                    {detailedTodo.status} ({detailedTodo.id}){' '}
                </SubTitle>
                <p>{detailedTodo.description}</p>
            </Wrapper>
        </>
    )
}

const Wrapper = styled.div`
  text-align: center;
  margin: 12px;
`

const SubTitle = styled.p`
  font-style: italic;
  font-size: 90%;
`
