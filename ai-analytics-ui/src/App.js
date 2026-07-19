import { useState } from "react";
import "./App.css";

import UploadComponent from "./components/UploadComponent";
import ChatComponent from "./components/ChatComponent";

function App() {

  const [tableName, setTableName] =
    useState("");

  return (
    <div className="app-container">

      <h1 className="title">
         🤖 AI Analytics Assistant
      </h1>

      <div className="dataset-info">
        Current Dataset:
        {" "}
        {
          tableName
            ? tableName
            : "No Dataset Uploaded"
        }
      </div>

      <UploadComponent
        setTableName={setTableName}
      />

      <ChatComponent
        tableName={tableName}
      />

    </div>
  );
}

export default App;