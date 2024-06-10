package org.codsoft;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SuppressWarnings("serial")
public class QuizApplication extends JFrame {
	private JLabel questionLabel;
	private ButtonGroup optionsGroup;
	private JRadioButton[] optionButtons;
	private JButton submitButton;
	private JLabel timerLabel;
	private Timer timer;

	private ArrayList<Question> questions;
	private int currentQuestionIndex;
	private int score;
	private int totalQuestions;

	private final int TIME_LIMIT = 30; // Time limit for each question in seconds

	public QuizApplication() {
		super("Java Quiz");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(500, 300);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		questionLabel = new JLabel();
		add(questionLabel, BorderLayout.NORTH);

		JPanel optionsPanel = new JPanel(new GridLayout(4, 1));
		optionsGroup = new ButtonGroup();
		optionButtons = new JRadioButton[4];
		for (int i = 0; i < 4; i++) {
			optionButtons[i] = new JRadioButton();
			optionsGroup.add(optionButtons[i]);
			optionsPanel.add(optionButtons[i]);
		}
		add(optionsPanel, BorderLayout.CENTER);

		submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitAnswer();
			}
		});
		add(submitButton, BorderLayout.SOUTH);

		timerLabel = new JLabel();
		add(timerLabel, BorderLayout.EAST);

		// Generate quiz questions
		generateQuestions();

		// Start the quiz
		startQuiz();
	}

	private void generateQuestions() {
		// Sample questions
		questions = new ArrayList<>();
		questions.add(new Question("What is the capital of Japan?",
				new String[] { "Tokyo", "Beijing", "Seoul", "Osaka" }, 0));
		questions.add(new Question("Which of the following is a programming language?",
				new String[] { "HTML", "CSS", "Java", "XML" }, 2));
		// Add more questions as needed

		totalQuestions = questions.size();
	}

	private void startQuiz() {
		currentQuestionIndex = 0;
		score = 0;
		displayQuestion();
		startTimer();
	}

	private void displayQuestion() {
		Question currentQuestion = questions.get(currentQuestionIndex);
		questionLabel.setText(currentQuestion.getQuestion());
		String[] options = currentQuestion.getOptions();
		for (int i = 0; i < options.length && i < 4; i++) {
			optionButtons[i].setText(options[i]);
			optionButtons[i].setSelected(false);
		}
	}

	private void startTimer() {
		timer = new Timer(1000, new ActionListener() {
			int timeLeft = TIME_LIMIT;

			@Override
			public void actionPerformed(ActionEvent e) {
				timerLabel.setText("Time Left: " + timeLeft + "s");
				timeLeft--;

				if (timeLeft < 0) {
					timer.stop();
					submitAnswer();
				}
			}
		});
		timer.start();
	}

	private void submitAnswer() {
		timer.stop(); // Stop the timer

		int selectedOption = -1;
		for (int i = 0; i < 4; i++) {
			if (optionButtons[i].isSelected()) {
				selectedOption = i;
				break;
			}
		}

		Question currentQuestion = questions.get(currentQuestionIndex);
		if (selectedOption == currentQuestion.getCorrectAnswerIndex()) {
			score++;
		}

		currentQuestionIndex++;
		if (currentQuestionIndex < totalQuestions) {
			displayQuestion();
			startTimer();
		} else {
			endQuiz();
		}
	}

	private void endQuiz() {
		JOptionPane.showMessageDialog(this, "Quiz Completed!\nYour Score: " + score + "/" + totalQuestions);
	}

	private class Question {
		private String question;
		private String[] options;
		private int correctAnswerIndex;

		public Question(String question, String[] options, int correctAnswerIndex) {
			this.question = question;
			this.options = options;
			this.correctAnswerIndex = correctAnswerIndex;
		}

		public String getQuestion() {
			return question;
		}

		public String[] getOptions() {
			return options;
		}

		public int getCorrectAnswerIndex() {
			return correctAnswerIndex;
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new QuizApplication().setVisible(true);
			}
		});
	}
}
