import './App.css'
import {createBrowserRouter, RouterProvider} from "react-router-dom";

import GamePage from "./components/game-page.tsx";
import StartPage from "./components/start-page.tsx";
import {ToastContainer} from "react-toastify";
import 'react-toastify/dist/ReactToastify.css';

const router = createBrowserRouter([
    {
        path: "/game/:id",
        element: <GamePage/>,
    }, {
        path: "",
        element: <StartPage/>,
    },
]);

function App() {
    return (
        <>
            <RouterProvider router={router}/>
            <ToastContainer />
        </>
    )
}

export default App
