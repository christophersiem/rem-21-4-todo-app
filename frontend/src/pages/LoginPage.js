import {useContext, useState} from 'react'
import {AuthContext} from '../context/AuthProvider'
import styled from "styled-components/macro";

const initialState = {
    username: '',
    password: '',
}

export default function LoginPage() {
    const [credentials, setCredentials] = useState(initialState)
    const {login} = useContext(AuthContext)
    const clientId = "bf030e78902c4107794b"

    const handleChange = event => {
        setCredentials({...credentials, [event.target.name]: event.target.value})
    }

    const handleSubmit = event => {
        event.preventDefault()
        login(credentials)
    }

    const loginWithGithub = () => {
        window.open("https://github.com/login/oauth/authorize?client_id=" + clientId)
    }

    return (
        <Form onSubmit={handleSubmit}>
            <label htmlFor="username">
                Username
            </label>
            <input
                type="text"
                required
                value={credentials.username}
                name="username"
                id="username"
                onChange={handleChange}
            />

            <label htmlFor="password">
                Passwort </label>
            <input
                type="password"
                required
                value={credentials.password}
                name="password"
                id="password"
                onChange={handleChange}
            />

            <button>Login</button>
            <button
                type={"button"}
                onClick={loginWithGithub}>Login with GitHub</button>
        </Form>
    )
}

const Form = styled.form`
  display: flex;
  justify-content: space-between;
  gap: 15px;
  align-items: center;
  flex-direction: column;

`