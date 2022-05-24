import java.util.ArrayList;

public class RoutingNode {
    public char NodeName;  //结点名称
    public char[] destination = new char[28];
    public int[] distance = new int[28];
    public char[] next = new char[28];
    public RoutingNode(char NodeName){
        this.NodeName = NodeName;
        for(int i = 0; i < 28; i++){
            distance[i] = 0;
        }
    }


}
