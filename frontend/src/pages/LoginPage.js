import {useState} from "react";

const initialState = {
    username: "",
    password: ""
}

export default function LoginPage({login}) {
    const [credentials, setCredentials] = useState(initialState);

    const handleChange = event => {
        setCredentials({...credentials, [event.target.name]: event.target.value})
    }

    const handleSubmit = event => {
        event.preventDefault()
        login(credentials)
    }

    return (
        <form onSubmit={handleSubmit}>
            <label> Username
                <input
                    type="text"
                    required
                    value={credentials.username}
                    name="username"
                    onChange={handleChange}
                />
            </label>
            <label> Passwort
                <input
                    type="password"
                    required
                    value={credentials.password}
                    name="password"
                    onChange={handleChange}
                />
            </label>
            <button>Login</button>
        </form>
    )
};