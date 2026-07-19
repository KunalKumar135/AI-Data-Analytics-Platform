import { useState } from "react";
import axios from "axios";

function ChatComponent({ tableName }) {

  const [question, setQuestion] = useState("");

  const [response, setResponse] = useState(null);

  const askQuestion = async () => {

    if (!tableName) {
      alert("Upload a dataset first");
      return;
    }

    if (!question.trim()) {
      alert("Please enter a question.");
      return;
    }

    try {
      const res = await axios.post(
        "http://localhost:8080/api/query/ask",
        {
          tableName,
          question
        }
      );

      setResponse(res.data);

      setResponse(res.data);
      // Clear the input after a successful response
      // setQuestion("");

    } catch (error) {
      console.error(error);
    }
  };

  return (
    <div className="chat-section">

      <h2
        style={{
          textAlign: "center",
          color: "#667eea",
          marginBottom: "15px"
        }}
      >
        Ask anything about your dataset
      </h2>

      <form
        className="question-box"
        onSubmit={(e) => {
          e.preventDefault(); // Prevent page refresh
          askQuestion();
        }}
      >
        <input
          className="question-input"
          type="text"
          placeholder="Ask anything about your dataset..."
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
        />

        <button
          className="send-btn"
          type="submit"
        >
          Send
        </button>
      </form>

      {response && (

        <div className="response-box">

          <div className="section-title">
            Answer
          </div>

          {
            response.result &&
            response.result.length > 1 ? (

              <div className="table-container">

                <table className="result-table">

                  <thead>

                    <tr>

                      {Object.keys(response.result[0]).map((key) => (

                        <th key={key}>
                          {key}
                        </th>

                      ))}

                    </tr>

                  </thead>

                  <tbody>

                    {response.result.map((row, index) => (

                      <tr key={index}>

                        {Object.values(row).map((value, i) => (

                          <td key={i}>
                            {value}
                          </td>

                        ))}

                      </tr>

                    ))}

                  </tbody>

                </table>

              </div>

            ) : (

              <p className="answer">
                {response.answer}
              </p>

            )
          }

          <div className="section-title">
            Explanation
          </div>

          <p>
            {response.explanation}
          </p>

          <div className="section-title">
            Generated SQL
          </div>

          <pre className="sql-box">
            {response.sql}
          </pre>

        </div>

      )}

    </div>
  );
}

export default ChatComponent;