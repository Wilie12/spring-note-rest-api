# Markdown Note-taking App API

A feature-rich RESTful API built with Spring Boot that allows users to upload, process, and render Markdown files. This project introduces complex text processing and file handling capabilities, including safe persistent local storage to avoid file name collisions (using UUIDs), database-backed metadata tracking, Markdown-to-HTML rendering using CommonMark, and automated grammar checking via an external LanguageTool service.

## Technologies Used

  * **Java**
  * **Spring Boot** (Web, RestClient, MVC)
  * **PostgreSQL** (For storing file metadata)
  * **CommonMark** (For Markdown parsing and HTML rendering)
  * **LanguageTool API** (External service for grammar checking)

-----

## Configuration & Setup

To run this application, you will need a PostgreSQL database and a running instance of a LanguageTool server. 

**Environment Variables:**
* `DB_USERNAME`: Your PostgreSQL username.
* `DB_PASSWORD`: Your PostgreSQL password.

**Application Properties / External Services:**
* **Storage:** Uploaded notes are saved to a local directory defined by `app.note-storage.basePath` (defaults to `./notes`). The API automatically generates UUIDs for stored files to prevent naming collisions. Only `text/plain` file types are allowed by default.
* **Grammar API:** The app expects a LanguageTool server running locally at `http://localhost:8010/v2/check`. You can easily spin this up using a LanguageTool Docker container.

-----

## API Reference

**Base URL:** `/api/v1/notes`

*(The application will start by default on `http://localhost:8080`)*

### 1\. Upload a Note

Accepts a Markdown or plain text file upload, saves it securely to the local file system using a UUID, and stores the file's metadata in the database.

  * **URL:** `/`
  * **Method:** `POST`
  * **Content-Type:** `multipart/form-data`
  * **Body:** A form-data part named `file` containing the file to upload.
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** `NoteResponse` (JSON)
  * **Error Responses:**
      * **Code:** `400 Bad Request` (If the file is empty or has an invalid mime type)
      * **Code:** `500 Internal Server Error` (If the file could not be stored)

### 2\. Check Note Grammar

Reads a previously uploaded note and sends its content to the external LanguageTool API to identify grammar, spelling, and stylistic errors.

  * **URL:** `/check/{noteId}`
  * **Method:** `GET`
  * **URL Parameters:** `noteId=[Long]` (Required)
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** `LanguageResponse` (JSON containing grammar matches and suggested replacements)
  * **Error Response:**
      * **Code:** `404 Not Found` (If the note metadata or file does not exist)

### 3\. View Rendered Note

Retrieves a stored Markdown note, parses the Markdown text, and renders it as HTML.

*(Note: Unlike the other REST endpoints, this endpoint returns an HTML view, making it suitable for direct browser rendering).*

  * **URL:** `/{noteId}`
  * **Method:** `GET`
  * **URL Parameters:** `noteId=[Long]` (Required)
  * **Success Response:**
      * **Code:** `200 OK`
      * **Content:** HTML Page (Includes the parsed Markdown)
  * **Error Response:**
      * **Code:** `404 Not Found`

-----

## Data Models

### NoteResponse

The payload returned after successfully uploading a file.
| Field | Type | Description |
| :--- | :--- | :--- |
| `noteId` | Long | The unique database identifier for the note's metadata |
| `originalName` | String | The original filename of the uploaded file |
| `size` | Long | The size of the file in bytes |

### LanguageResponse

The wrapper payload containing the results of the grammar check.
| Field | Type | Description |
| :--- | :--- | :--- |
| `matches` | List\<LanguageMatch\> | An array of grammar/spelling issues found in the text |

### LanguageMatch

Details about a specific grammar or spelling error identified in the text.
| Field | Type | Description |
| :--- | :--- | :--- |
| `message` | String | Explanation of the rule violation or error |
| `replacements` | List\<Replacement\> | Suggested fixes for the error |
| `offset` | int | The starting character index of the error in the text |
| `length` | int | The length of the erroneous text segment |
| `sentence` | String | The full sentence containing the error |

### Replacement

A suggested correction for a specific grammar match.
| Field | Type | Description |
| :--- | :--- | :--- |
| `value` | String | The corrected text to replace the error |
