import {useLocation} from "react-router-dom";
import {useContext, useEffect} from "react";
import {AuthContext} from "../context/AuthProvider";

export default function GithubRedirectPage() {

    const queryParameter = new URLSearchParams(useLocation().search)
    const code = queryParameter.get("code")
    const {loginWithGithub} = useContext(AuthContext)

    useEffect(() => {
        loginWithGithub(code)
    }, [code])

    return (
        <div>
            <p>Bitte warten Sie, der nächste freie Mitarbeiter ist gleich für Sie da :)</p>
        </div>
    )
};