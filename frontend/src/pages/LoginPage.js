import {useContext, useState} from 'react'
import {AuthContext} from '../context/AuthProvider'

const initialState = {
    username: '',
    password: '',
}

export default function LoginPage() {
    const [credentials, setCredentials] = useState(initialState)
    const {login} = useContext(AuthContext)

    /* TODO Get from backend */
    const clientId = "48fab296905327059b57"
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
        <form onSubmit={handleSubmit}>
            <label>
                {' '}
                Username
                <input
                    type="text"
                    required
                    value={credentials.username}
                    name="username"
                    onChange={handleChange}
                />
            </label>
            <label>
                {' '}
                Passwort
                <input
                    type="password"
                    required
                    value={credentials.password}
                    name="password"
                    onChange={handleChange}
                />
            </label>
            <button>Login</button>
            <button onClick={loginWithGithub}>Login with Github</button>
        </form>
    )
}
