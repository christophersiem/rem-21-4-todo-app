import styled from "styled-components/macro";
import {Link} from "react-router-dom";

export default function Header() {

    return (
        <HeaderStyled>
            <h2>Super Todo App</h2>
            <nav>
                <ListStyled>
                    <LiStyled>
                        <Link to="/">Home</Link>
                    </LiStyled>
                    <LiStyled>
                        <Link to="/open">Open</Link>
                    </LiStyled>
                    <LiStyled>
                        <Link to="/inprogress">In Progress</Link>
                    </LiStyled>
                    <LiStyled>
                        <Link to="/done">Done</Link>
                    </LiStyled>
                </ListStyled>
            </nav>
        </HeaderStyled>

    )
}

const HeaderStyled = styled.header`
    text-align: center;
`

const ListStyled = styled.ul`
    list-style: none;
    padding: 0;
  height: min-content;
`

const LiStyled = styled.li`
    display: inline;
  margin: 10px;
`