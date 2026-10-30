import { useEffect, useRef, useState } from "react";
import "./App.css";

interface ChatMessage {
  role: "user" | "assistant";
  content: string;
}

interface ChatResponse {
  reply: string;
}

function App() {
  const [messages, setMessages] = useState<ChatMessage[]>([]);
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(false);

  const messagesEndRef = useRef<HTMLDivElement>(null);

  // Automatically scroll to the latest message
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({
      behavior: "smooth",
    });
  }, [messages, loading]);

  const sendMessage = async () => {
    const message = input.trim();

    if (!message || loading) {
      return;
    }

    // Display the user's message immediately
    setMessages((previous) => [
      ...previous,
      {
        role: "user",
        content: message,
      },
    ]);

    setInput("");
    setLoading(true);

    try {
      const response = await fetch(
        "http://localhost:8080/api/chat",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify({
            message: message,
          }),
        }
      );

      const responseText = await response.text();

      let data: Partial<ChatResponse> & {
        message?: string;
        error?: string;
      } = {};

      try {
        data = JSON.parse(responseText);
      } catch {
        // Response was not JSON
      }

      if (!response.ok) {
        throw new Error(
          data.message ||
          data.error ||
          `Request failed with status ${response.status}`
        );
      }

      if (!data.reply) {
        throw new Error("The backend returned an empty response.");
      }

      // Display the AI response
      setMessages((previous) => [
        ...previous,
        {
          role: "assistant",
          content: data.reply!,
        },
      ]);
    } catch (error) {
      console.error("Chat API error:", error);

      setMessages((previous) => [
        ...previous,
        {
          role: "assistant",
          content:
            error instanceof Error
              ? `Sorry, something went wrong: ${error.message}`
              : "Unable to connect to the server.",
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleKeyDown = (
    event: React.KeyboardEvent<HTMLTextAreaElement>
  ) => {
    if (event.key === "Enter" && !event.shiftKey) {
      event.preventDefault();
      sendMessage();
    }
  };

  return (
    <div className="app-container">
      <header className="chat-header">
        <div className="brand">
          <div className="brand-icon">AI</div>

          <div>
            <h2>AI Assistant</h2>
            <span>Powered by OpenAI</span>
          </div>
        </div>

        <div className="status">
          <span className="status-dot"></span>
          Ready to chat
        </div>
      </header>

      <main className="chat-container">
        {messages.length === 0 ? (
          <div className="welcome-screen">
            <div className="welcome-icon">✦</div>

            <h1>How can I help you today?</h1>

            <p>
              Ask me anything. I'm here to help you
              learn, explore and solve problems.
            </p>
          </div>
        ) : (
          <div className="messages-container">
            {messages.map((message, index) => (
              <div
                key={index}
                className={`message-row ${message.role}`}
              >
                <div className="message-avatar">
                  {message.role === "user" ? "U" : "AI"}
                </div>

                <div className="message-content">
                  <span className="message-name">
                    {message.role === "user"
                      ? "You"
                      : "AI Assistant"}
                  </span>

                  <div className="message-bubble">
                    {message.content}
                  </div>
                </div>
              </div>
            ))}

            {loading && (
              <div className="message-row assistant">
                <div className="message-avatar">AI</div>

                <div className="message-content">
                  <span className="message-name">
                    AI Assistant
                  </span>

                  <div className="message-bubble typing">
                    <span></span>
                    <span></span>
                    <span></span>
                  </div>
                </div>
              </div>
            )}

            <div ref={messagesEndRef}></div>
          </div>
        )}
      </main>

      <footer className="chat-footer">
        <div className="input-container">
          <textarea
            value={input}
            onChange={(event) => setInput(event.target.value)}
            onKeyDown={handleKeyDown}
            placeholder="Message AI Assistant..."
            rows={1}
            disabled={loading}
          />

          <button
            className="send-button"
            onClick={sendMessage}
            disabled={!input.trim() || loading}
            aria-label="Send message"
          >
            ↑
          </button>
        </div>

        <p className="footer-note">
          AI can make mistakes. Please verify important information.
          Press Enter to send, Shift + Enter for a new line.
        </p>
      </footer>
    </div>
  );
}

export default App;