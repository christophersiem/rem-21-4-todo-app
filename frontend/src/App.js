import Header from './components/Header'
import styled from 'styled-components/macro'

import NavigationBar from './components/NavigationBar'
import { Route, Switch } from 'react-router-dom'
import Homepage from './pages/Homepage'
import BoardPage from './pages/BoardPage'
import DetailsPage from './pages/DetailsPage'
import useTodos from './hooks/useTodos'
import LoginPage from './pages/LoginPage'
import AuthProvider from './context/AuthProvider'

function App() {
  const { todos, addTodo, advanceTodo, removeTodo } = useTodos()

  return (
    <AuthProvider>
      <PageLayout>
        <Header />
        <NavigationBar />
        <Switch>
          <Route path={'/login'}>
            <LoginPage />
          </Route>
          <Route path="/" exact>
            <Homepage
              todos={todos}
              onAdvance={advanceTodo}
              onDelete={removeTodo}
              onAdd={addTodo}
            />
          </Route>
          <Route path="/todos/:statusSlug">
            <BoardPage
              todos={todos}
              onAdvance={advanceTodo}
              onDelete={removeTodo}
            />
          </Route>
          <Route path={'/todo/:id'}>
            <DetailsPage />
          </Route>
        </Switch>
      </PageLayout>
    </AuthProvider>
  )
}

export default App

const PageLayout = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: grid;
  grid-template-rows: min-content min-content 1fr min-content;
`
