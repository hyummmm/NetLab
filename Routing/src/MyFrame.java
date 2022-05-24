import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyFrame extends JFrame {
    public static JFrame jf = new JFrame();  //创建一个JFrame
    public static JLabel jLabel = new JLabel("请输入路由节点个数n(0<n<=28)：");
    public static JTextField textField = new JTextField(16);
    public static JButton jButton = new JButton("生成拓扑");
    public static void Init(){
        jf.setTitle("距离矢量路由算法模拟");
        int panel_width = 800;
        int panel_height = 600;
        jf.setSize(panel_width, panel_height);   //设置窗口的大小
        int width = (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()-panel_width)/2; //定位窗口的宽度
//        int height = (int) (Toolkit.getDefaultToolkit().getScreenSize().getHeight() - panel_height / 2); //定位窗口的高度
        jf.setLocation(width, 150);     //设置窗口的位置
        jf.addMouseListener(new MyListener());  //为窗口添加鼠标监听
        jf.setResizable(false);            //设置不可改变大小

        BorderLayout border_layout = new BorderLayout();
        jf.setLayout(border_layout);//设置边框布局

        JPanel jPanel_north = new JPanel();

        jButton.addActionListener(new ButtonListener());

        jPanel_north.add(jLabel);
        jPanel_north.add(textField);
        jPanel_north.add(jButton);

        jf.add(jPanel_north, BorderLayout.PAGE_START);//添加JPanel到窗口

        jf.setVisible(true);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MyFrame::Init); //初始化
    }
}

class MyListener extends MouseAdapter {
    //这两组x和y为鼠标点下时在屏幕的位置和拖动时所在的位置
    int newX,newY,oldX,oldY;
    //这两个坐标为组件当前的坐标
    int startX,startY;

    @Override
    public void mousePressed(MouseEvent e) {
        //此为得到事件源组件
        Component cp = (Component)e.getSource();
        //当鼠标点下的时候记录组件当前的坐标与鼠标当前在屏幕的位置
        startX = cp.getX();
        startY = cp.getY();
        oldX = e.getXOnScreen();
        oldY = e.getYOnScreen();
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        Component cp = (Component)e.getSource();
        //拖动的时候记录新坐标
        newX = e.getXOnScreen();
        newY = e.getYOnScreen();
        //设置bounds,将点下时记录的组件开始坐标与鼠标拖动的距离相加
        cp.setBounds(startX+(newX - oldX), startY+(newY - oldY), cp.getWidth(), cp.getHeight());
    }
}

class ButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String s = MyFrame.textField.getText();
        int n = Integer.parseInt(s);
        if(n <= 0 || n > 28){
            JOptionPane.showMessageDialog(null, "输入数字不在范围内！请重新输入");
            MyFrame.textField.setText("");
        }else{
            Algorithm a = new Algorithm(n);
            Object[] columnNames = new Object[n + 1];
            columnNames[0] = "";
            for(int i = 1; i <= n; i++){
                columnNames[i] = a.node[i-1].NodeName;
            }
            Object[][] rowData = new Object[n][n + 1];
            for(int i = 0; i < n; i++){
                rowData[i][0] = a.node[i].NodeName;
            }
            for(int i = 0; i < n; i++){
                for(int j = 1; j <= n; j++){
                    if(a.node[i].distance[j-1] == 1000){
                        rowData[i][j] = "∞";
                    }else{
                        rowData[i][j] = a.node[i].distance[j-1];
                    }
                }
            }
            JPanel jPanel_center = new JPanel();
            jPanel_center.setPreferredSize(new Dimension(750,320));
            JTable jTable = new JTable(rowData, columnNames);
            // 设置表格居中显示
            DefaultTableCellRenderer dc=new DefaultTableCellRenderer();
            dc.setHorizontalAlignment(JLabel.CENTER);
            jTable.setDefaultRenderer(Object.class, dc);
            // 设置表格显示表头
            JScrollPane scrollPane = new JScrollPane(jTable);
            jTable.setPreferredScrollableViewportSize(new Dimension(750, 150));
            jPanel_center.add(scrollPane);

            JButton jButton_exchange = new JButton("路由交换");
            jButton_exchange.setPreferredSize(new Dimension(300,30));
            jButton_exchange.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int count = a.exchange();
                    JLabel count_label = new JLabel("已经达到稳定状态，一共运行了" + count +"轮");
                    Object[] columnNames2 = new Object[n + 1];
                    columnNames2[0] = "";
                    for(int i = 1; i <= n; i++){
                        columnNames2[i] = "目的地" + a.node[i-1].NodeName;
                    }
                    Object[][] rowData2 = new Object[n + 1][n + 1];
                    for(int j = 1; j <= n; j++){
                        rowData2[0][j] = "距离 下一跳";
                    }
                    for(int i = 1; i < n + 1; i++){
                        rowData2[i][0] = a.node[i-1].NodeName;
                    }
                    for(int i = 1; i < n + 1; i++){
                        for(int j = 1; j <= n; j++){
                            if(a.node[i-1].distance[j-1] == 1000){
                                rowData2[i][j] = "∞";
                            }else if(a.node[i-1].distance[j-1] == 0){
                                rowData2[i][j] = "";
                            }else {
                                rowData2[i][j] = String.valueOf(a.node[i-1].distance[j-1]) + " " +a.node[i-1].next[j-1];
                            }
                        }
                    }
                    JTable exchangeTable = new JTable(rowData2,columnNames2);
                    JPanel jPanel_south = new JPanel();
                    jPanel_south.setPreferredSize(new Dimension(750,300));
                    // 设置表格居中显示
                    exchangeTable.setDefaultRenderer(Object.class, dc);
                    // 设置表格显示表头
                    JScrollPane scrollPane2 = new JScrollPane(exchangeTable);
                    exchangeTable.setPreferredScrollableViewportSize(new Dimension(750, 150));
                    jPanel_south.add(count_label);
                    jPanel_south.add(scrollPane2);
                    MyFrame.jf.add(jPanel_south,BorderLayout.SOUTH);
                    MyFrame.jf.setVisible(true);
                }
            });
            jPanel_center.add(jButton_exchange);
            //添加JPanel到窗口
            MyFrame.jf.add(jPanel_center, BorderLayout.CENTER);
            MyFrame.jf.setVisible(true);
        }
    }
}


