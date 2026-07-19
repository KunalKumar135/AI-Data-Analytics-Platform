import { useState } from "react";
import axios from "axios";

function UploadComponent({
  setTableName
}) {

  const [file, setFile] =
    useState(null);

  const [message, setMessage] =
    useState("");

  const handleUpload = async () => {

    if (!file) {
      alert("Select a file");
      return;
    }

    const formData =
      new FormData();

    formData.append(
      "file",
      file
    );

    try {

      const response =
        await axios.post(
          "http://localhost:8080/api/datasets/upload",
          formData
        );

      setTableName(
        response.data.tableName
      );

      setMessage(
        "Uploaded Successfully"
      );

    } catch (error) {

      console.error(error);

      setMessage(
        "Upload Failed"
      );
    }
  };

  return (
    <div className="upload-section">

      <input
        type="file"
        onChange={(e) =>
          setFile(
            e.target.files[0]
          )
        }
      />

      <button
        className="upload-btn"
        onClick={handleUpload}
      >
        Upload
      </button>

      <p>{message}</p>

    </div>
  );
}

export default UploadComponent;