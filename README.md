# Sørensen–Dice Text Similarity Comparator

**@author** Artem Vasylevytskyi  
**@version** Java 23

---

## Project Overview

This project is a console-based Java application developed as part of the **Software Design & Data Structures – Main Assignment**.

The application performs rapid comparison of two text files by computing the **Sørensen–Dice similarity coefficient**. It supports optional stop-word filtering, noise ratio analysis, and utilizes modern Java concurrency features, including virtual threads, to efficiently process large text files.

The system is designed to be simple to use and efficient, while clearly demonstrating core software design principles.

## Repository

The full source code for this project is available on GitHub:

**https://github.com/artem-totality/sorenser-dice**

---

## Key Features

### Sørensen–Dice Similarity Calculation

Computes the similarity between two texts using the Sørensen–Dice coefficient, a well-established metric for comparing sets of tokens. The result is a normalized value between 0.0 and 1.0, where higher values indicate greater similarity.

### Token-Based Text Preprocessing

Each input text is normalized before comparison by:

- Converting all characters to lower case
- Removing punctuation and special characters
- Splitting the text into individual word tokens

This ensures consistent and fair comparison regardless of formatting differences.

### Set-Based Comparison (Unique Tokens)

Tokens are stored in sets, meaning duplicate words are counted only once. This design choice focuses the comparison on vocabulary overlap rather than word frequency.

### Optional Stop-Word Filtering

The application supports loading a custom stop-word list (e.g. common English words). When enabled, these low-information tokens are removed from the comparison to improve semantic relevance.

### Noise Ratio Analysis

Calculates the proportion of stop-words within each text and provides a clear interpretation of whether filtering is:

- Unnecessary
- Optional
- Highly recommended

This helps users make an informed decision rather than blindly enabling filters.

### Minimum Token Safety Checks

Ensures that a meaningful comparison is only performed when a sufficient number of tokens remains after preprocessing and filtering, preventing misleading similarity scores.

### Multithreaded Text Processing

Utilizes modern Java virtual threads to preprocess text files concurrently, significantly improving performance when working with large files.

### Interactive Console-Based Menu

Provides a user-friendly command-line interface for:

- Uploading files
- Toggling filtering
- Running comparisons
- Viewing system status

### ANSI-Colored Console Output with Progress Indicators

Enhances usability by displaying colored status messages and real-time progress bars during intensive operations, improving clarity and user feedback.

---

## Building the Project

To compile the project and create the executable JAR file, run the build script:

```bash
./build.sh
```

This will:

1. Compile all Java source files from `src/ie/atu/sw/`
2. Create an executable JAR file named `dice.jar`

**Requirements:**

- Java 23 or higher
- Preview features enabled

---

## Running the Application

The project is distributed as an executable JAR file.

### Run Command:

```bash
java --enable-preview -jar dice.jar
```

Alternatively, use the run script:

```bash
./run.sh
```

---

## Requirements

- Java 23 or higher
- Terminal with ANSI color support (recommended)

---

## Usage

1. Launch the application using the command above
2. Follow the interactive menu to upload two text files
3. Optionally enable stop-word filtering
4. Run the comparison to view the similarity score
5. Review noise ratio analysis and recommendations

---

## License

This project is developed for educational purposes as part of a university assignment.
