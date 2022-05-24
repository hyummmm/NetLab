import java.util.Scanner;

public class Algorithm {
    public RoutingNode[] node = new RoutingNode[28];
    public int n;
    public Algorithm(int n){
        this.n = n;
        createRoutingTable();
    }
    public void createRoutingTable(){
        for(int i = 0; i < n; i++){
            node[i] = new RoutingNode((char)(i + 65));
        }
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i == j){
                    node[i].distance[j] = 0;
                }else {
                    if(node[i].distance[j] == 0){
                        node[i].distance[j] = (int)(Math.random()*50);
                        node[j].distance[i] = node[i].distance[j];
                        if(node[i].distance[j] > 20 || node[i].distance[j] == 0){//这种情况视为不相邻把距离设为一个很大的值
                            node[i].distance[j]=1000;
                            node[j].distance[i]=1000;
                        }
                    }
                }
                node[i].destination[j] = (char)(j + 65);
                node[i].next[j] = (char)(j + 65);
            }
        }
        // 后台展示
        System.out.println("********************路由表初始状态********************");
        for(int i = 0; i < n; i++){
            System.out.println("当前节点为：" + node[i].NodeName + "，下方为它的路由表：");
            System.out.println("目的地\t花费\t下一跳");
            for(int j = 0; j < n; j++){
//                System.out.println(node[i].destination[j] + "\t" +node[i].distance[j] + "\t" +node[i].next[j]);
                System.out.print(node[i].destination[j] + "\t");
                if(node[i].distance[j] == 1000){
                    System.out.println("不可达\t");
                }else{
                    System.out.println(node[i].distance[j] + "\t" +node[i].next[j]);
                }

            }
        }
    }

    public int exchange(){
        int count = 0;
        boolean flag = false;
        while(true){
            for(int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    for (int k = 0; k < n; k++) {
                        if (node[i].distance[k] == 1000) {
                            continue;   // 不相邻的结点不交换路由表
                        }
                        if (node[i].distance[j] > node[i].distance[k] + node[k].distance[j]) {
                            node[i].distance[j] = node[i].distance[k] + node[k].distance[j];
                            node[i].next[j] = node[k].NodeName;
                            flag = true;
                        }
                    }
                }
            }
            count++;
            // 后台展示
            System.out.println("********************路由表交换第" + count +"轮结果********************");
            for(int i = 0; i < n; i++){
                System.out.println("当前节点为：" + node[i].NodeName + "，下方为它的路由表：");
                System.out.println("目的地\t花费\t下一跳");
                for(int j = 0; j < n; j++){
//                System.out.println(node[i].destination[j] + "\t" +node[i].distance[j] + "\t" +node[i].next[j]);
                    System.out.print(node[i].destination[j] + "\t");
                    if(node[i].distance[j] == 1000){
                        System.out.println("不可达\t");
                    }else{
                        System.out.println(node[i].distance[j] + "\t" +node[i].next[j]);
                    }
                }
            }
            if(!flag){
                System.out.println("已经达到稳定状态，一共运行了"+count+"轮");
                return count;
            }
            flag = false;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        Algorithm a = new Algorithm(n);
        a.exchange();

    }
}
