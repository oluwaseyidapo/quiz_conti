package quiz;
import javax.swing.*;  
import quiz.listener.CustomRadioButtonListener;
import quiz.listener.InputRadioButtonListener;
import quiz.question.InputQuestion;
import quiz.question.MultipleChoiceQuestion;
import quiz.question.Question;
import quiz.question.Question.QType;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
public class Testing extends JFrame{
	private final String QUESTION_PATH = "./Questions.csv"; 
	private List<Question> questions;
	public static final int X= 1000;
	public static final int Y=950;
	public static final int X_ORIGIN= 150;
	public static final int Y_ORIGIN= 250;
	public static Scanner scan;
	static JTextField text ;
	static JButton button;
	JLabel label;
	 
	Container contentPane;
	Question question;
	public static void main(String [] args) throws Exception {
		Testing test= new Testing();
		test.setVisible(true);
		
	}

	/**
	 * Read questions from file
	 * @return List<Question>
	 * @throws Exception
	 */
	public List<Question> reader() throws Exception {
		
		File infile = new File(QUESTION_PATH);
		
		FileReader reader = new FileReader(infile);
		
		BufferedReader bufrea = new BufferedReader(reader);
		
		List<Question> collection = new ArrayList<>();
		
		List<String> x = bufrea.lines().collect(Collectors.toList());
		
		for (int i = 0; i < x.size(); i++) {
			String[] content = x.get(i).split(",");
			if(i != 0) {
		
				if(QType.INPUT.getValue().equals(content[1])) {
					collection.add(new InputQuestion(content[0], QType.INPUT, content[6]));
				} else if(QType.MULTI.getValue().equals(content[1])) {
					collection.add(new MultipleChoiceQuestion(content[0], QType.MULTI, content[6],content[2],content[3],content[4],content[5]));
				} else {
					throw new Exception("Wrong question type");
				}
			}
		}
		
		bufrea.close();
		reader.close();
		
		return collection;
	}

	public JPanel initQuestion(Question question) {
		
		JPanel panel = new JPanel(new GridLayout(5,1));
		JLabel label = new JLabel(question.getQuestion());
		panel.add(label);
		switch (question.getType()) {
		case MULTI:
			MultipleChoiceQuestion m = (MultipleChoiceQuestion) question;
			List<String> options = Arrays.asList(m.getOption1(), m.getOption2(), m.getOption3(),m.getOption4());
			ButtonGroup group = new ButtonGroup();
			options.forEach(op ->{
				JRadioButton b = new JRadioButton(op);
				b.addActionListener(new CustomRadioButtonListener(m,op));
				if(op.equals(question.getAnswer())) {
					List <String> right= new ArrayList<String>();
			    right.add(op);
				}else {
				List <String> wrong= new ArrayList<String>();
				wrong.add(op);
				}
				group.add(b);
				panel.add(b);
			});
			
			return panel;
		case INPUT:
			InputQuestion i = (InputQuestion) question;
			JTextField text = new JTextField();
			text.addInputMethodListener(new InputRadioButtonListener(question, question.getAnswer()));
			
			if(text.equals(question.getAnswer())) {
				List <JTextField> right= new ArrayList<JTextField>();
		    right.add(text);
			}else {
			List <JTextField> wrong= new ArrayList<JTextField>();
			wrong.add(text);
			}
			panel.add(text);
			return panel;
			
		default:
			throw new RuntimeException("Invalid type");
		}
	}
	public Testing() throws Exception {
		this.questions = reader();
		if(this.questions.isEmpty()) {
			throw new Exception("U need questions to play the game");
			}
		contentPane=getContentPane();
		setSize(X,Y);
		setTitle("Gui Games");
		setLocation(X_ORIGIN,Y_ORIGIN);
		contentPane.setBackground(Color.WHITE);
		contentPane.setLayout(new GridLayout(5,0));
		questions.forEach(x ->{
			contentPane.add(initQuestion(x), BorderLayout.CENTER); 
		});
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
}