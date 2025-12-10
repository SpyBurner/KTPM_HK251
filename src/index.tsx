import { createRoot } from "react-dom/client";
import App from "./App";
import { BrowserRouter } from 'react-router-dom';

createRoot(document.getElementById("app") as HTMLElement).render(
  <BrowserRouter>
    <App/>
  </BrowserRouter>,
);
