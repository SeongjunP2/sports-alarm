package view;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import controller.SportsTeamDao;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Calendar;
import java.util.List;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class SportsAlarmBaseball {

	private JFrame frame;
	private JPanel panelCal;
	
	private JPanel panelCalButton;
	private JButton btnPrevMonth;
	private JTextPane txtpnMonth;
	private JButton btnNextMonth;
	protected JPanel panelCalMain;
	
	protected Calendar cal = Calendar.getInstance();
	private JPanel panelContent;
	private JTextField textDate;
	private JPanel panelTeam;
	private JComboBox<String> comboBoxTeam;
	private JLabel lblNewLabel;
	
	protected String[] week = {"일", "월", "화", "수", "목", "금", "토"};
	private JButton btnBack;
	
    private SportsTeamDao dao = SportsTeamDao.getInstance();
    private String[] COLUMN_TEAMS;
	
	//파라미터: 색상, 선 두께, border의 모서리를 둥글게 할 것인지
	private LineBorder border = new LineBorder(Color.black, 1, true); 
	private JButton btnSchedule;
	private JScrollPane scrollPane;
	private JTable tableSchedule;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
				    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel"); // Swing 디자인을 LookAndFeel 테마로 변경
				    SportsAlarmBaseball window = new SportsAlarmBaseball();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SportsAlarmBaseball() {
		initialize();
		createCalendar();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 646, 489);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		panelCal = new JPanel();
		panelCal.setBounds(12, 43, 606, 396);
		frame.getContentPane().add(panelCal);
		panelCal.setLayout(null);
		
		panelCalButton = new JPanel();
		panelCalButton.setBounds(0, 0, 606, 37);
		panelCal.add(panelCalButton);
		panelCalButton.setBorder(border);
		
		panelCalMain = new JPanel();
		panelCalMain.setBounds(0, 37, 398, 359);
		panelCal.add(panelCalMain);
		panelCalMain.setLayout(new GridLayout(0, 7, 0, 0)); // panelCalMain에 7만큼만 출력
		
		btnPrevMonth = new JButton("Prev");
		btnPrevMonth.setFont(new Font("굴림", Font.PLAIN, 14));
		btnPrevMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.MONTH, -1);
				createCalendar();
			}
		});
		panelCalButton.add(btnPrevMonth);
		
		btnNextMonth = new JButton("Next");
		btnNextMonth.setFont(new Font("굴림", Font.PLAIN, 14));
		btnNextMonth.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cal.add(Calendar.MONTH, 1); // 다음 달로 이동
                createCalendar();
            }
        });
        
        txtpnMonth = new JTextPane();
        txtpnMonth.setFont(new Font("굴림", Font.PLAIN, 14));
        txtpnMonth.setText(Integer.toString(cal.get(Calendar.MONTH) + 1));
        panelCalButton.add(txtpnMonth);
        panelCalButton.add(btnNextMonth);
        
        panelContent = new JPanel();
        panelContent.setBounds(398, 37, 208, 359);
        panelCal.add(panelContent);
        panelContent.setBorder(border);
        panelContent.setLayout(null);
        
        textDate = new JTextField();
        textDate.setBounds(6, 6, 106, 22);
        textDate.setHorizontalAlignment(SwingConstants.CENTER);
        textDate.setFont(new Font("굴림", Font.PLAIN, 13));
        panelContent.add(textDate);
        textDate.setColumns(10);
        
        btnSchedule = new JButton("일정 추가");
        btnSchedule.setBounds(117, 6, 85, 23);
        btnSchedule.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                // 일정 추가 다이얼로그 생성
                JFrame dialogFrame = new JFrame();
                dialogFrame.setBounds(100, 100, 300, 200);
                dialogFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                dialogFrame.getContentPane().setLayout(new BorderLayout());
                
                JPanel panel = new JPanel();
                dialogFrame.getContentPane().add(panel, BorderLayout.CENTER);
                panel.setLayout(new GridLayout(0, 2, 0, 0));
                
                JLabel lblTime = new JLabel("시간:");
                panel.add(lblTime);
                
                JComboBox<Integer> comboBoxHour = new JComboBox<>();
                for (int hour = 0; hour <= 23; hour++) {
                    comboBoxHour.addItem(hour);
                }
                panel.add(comboBoxHour);
                
                JLabel lblMinute = new JLabel("분:");
                panel.add(lblMinute);
                
                JComboBox<Integer> comboBoxMinute = new JComboBox<>();
                for (int minute = 0; minute <= 59; minute++) {
                    comboBoxMinute.addItem(minute);
                }
                panel.add(comboBoxMinute);
                
                JButton btnAddSchedule = new JButton("일정 추가");
                btnAddSchedule.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // 선택한 시간과 분을 가져옴
                        int selectedHour = (int) comboBoxHour.getSelectedItem();
                        int selectedMinute = (int) comboBoxMinute.getSelectedItem();
                        // 선택한 날짜 가져옴
                        String selectedDate = textDate.getText().trim();
                        // 선택한 팀 가져옴
                        String selectedTeam = (String) comboBoxTeam.getSelectedItem();
                        // 일정 추가 작업 수행
                        addSchedule(selectedDate, selectedTeam, selectedHour, selectedMinute);
                        // panelContent의 레이아웃을 BorderLayout으로 설정
                        panelContent.setLayout(new BoxLayout(panelContent, BoxLayout.Y_AXIS));
                        // 추가한 일정을 패널에 표시
                        JLabel lblSchedule = new JLabel(selectedDate + " " + selectedTeam + " " + selectedHour + ":" + selectedMinute);
                        tableSchedule.add(lblSchedule);
                        // 패널을 다시 그리기
                        panelContent.revalidate();
                        panelContent.repaint();
                        // 다이얼로그 닫기
                        dialogFrame.dispose();
                    }
                });
                panel.add(btnAddSchedule);
                
                dialogFrame.setVisible(true);
            }
        });
        btnSchedule.setFont(new Font("Gulim", Font.PLAIN, 12));
        panelContent.add(btnSchedule);
        
        scrollPane = new JScrollPane();
        scrollPane.setBounds(6, 38, 196, 311);
        panelContent.add(scrollPane);
        
        tableSchedule = new JTable();
        scrollPane.setViewportView(tableSchedule);
        
        panelTeam = new JPanel();
        panelTeam.setBounds(12, 10, 519, 33);
        frame.getContentPane().add(panelTeam);
        
        lblNewLabel = new JLabel("팀선택");
        lblNewLabel.setFont(new Font("굴림", Font.BOLD, 18));
        panelTeam.add(lblNewLabel);
        
        comboBoxTeam = new JComboBox<>();
        comboBoxTeam.setFont(new Font("굴림", Font.PLAIN, 16));
        List<String> teamNames = dao.getTeamsByBaseball("KBO(한국 프로야구)");
        COLUMN_TEAMS = teamNames.toArray(new String[0]);
        final DefaultComboBoxModel<String> comboBoxModel = 
                new DefaultComboBoxModel<>(COLUMN_TEAMS);
        comboBoxTeam.setModel(comboBoxModel);
        panelTeam.add(comboBoxTeam);
        
        btnBack = new JButton("Back");
        btnBack.setFont(new Font("굴림", Font.PLAIN, 12));
        btnBack.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		SportsAlarmMain sam = new SportsAlarmMain();
        		sam.setVisible(true);
				frame.setVisible(false);
        	}
        });
        btnBack.setBounds(533, 10, 85, 33);
        frame.getContentPane().add(btnBack);
	}
	

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}
	
    private void createCalendar() {
        panelCalMain.removeAll(); // 이전에 생성된 캘린더 버튼들을 제거
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        cal.set(Calendar.DAY_OF_MONTH, 1); // 달의 첫째 날로 설정

        for (String day : week) {
            JLabel dayLabel = new JLabel(day);
            dayLabel.setBorder(border);
            dayLabel.setFont(new Font("굴림", Font.BOLD, 17));
            dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
            panelCalMain.add(dayLabel); // 요일 표시를 위한 레이블 추가
        }

        int startDay = cal.get(Calendar.DAY_OF_WEEK); // 첫째 날의 요일
        int monthDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 달의 총 일 수

        for (int i = 1; i < startDay; i++) {
            panelCalMain.add(new JLabel("")); // 첫째 날 이전의 빈 레이블 추가
        }

        for (int i = 1; i <= monthDay; i++) {
        	JButton btnDay = new JButton(Integer.toString(i));
        	btnDay.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent e) {
            		textDate.setText(
            				year + "년 " + 
            				(month + 1) + "월 " + 
            				btnDay.getText() + "일 ");
            	}
            });
            panelCalMain.add(btnDay);
        }
        
        txtpnMonth.setText(Integer.toString(month + 1) + "월"); // 현재 월 갱신

        frame.revalidate();
        frame.repaint();
    }
    
    
	// 일정을 추가하는 메서드
	private void addSchedule(String date, String team, int hour, int minute) {
		// 여기에 일정을 추가하는 코드를 작성하면 됩니다.
		// 예를 들어, 이 메서드를 통해 데이터베이스에 일정을 추가하거나,
		// 다른 방법으로 일정을 저장하고 처리할 수 있습니다.
		// 이 예제에서는 그냥 콘솔에 출력하는 것으로 가정합니다.
		System.out.println("일정 추가: " + date + " " + team + " " + hour + ":" + minute);
	}
}
