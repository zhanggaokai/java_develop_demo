package 数据结构与算法.线性结构.链表;

public class SingleLinkList {
    public static void main(String[] args) {
        LinkList l1=new LinkList();
        Node node1=new Node(1,"宋江","及时雨");
        Node node2=new Node(2,"卢俊义","玉麒麟");
        Node node3=new Node(3,"吴用","智多星");
        Node node4=new Node(4,"林冲","豹子头");
        l1.add(node1);
        l1.add(node2);
        l1.add(node3);
        l1.add(node4);
        l1.delete(node2);
        l1.delete(node1);
        l1.delete(node3);
        l1.delete(node4);
        l1.list();
    }
    /**定义一个单链表*/
    static class  LinkList{
        //初始化一个头节点head
        private Node head=new Node(0,"","");
        //添加节点
        public void add(Node node){
            Node tmp=head;
            //遍历链表,找到最后
            while(true){
                if(tmp.next==null){
                    break;
                }
                tmp=tmp.next;
            }
            //推出循环,tmp就是最后的节点.
            tmp.next=node;
        }
        /**删除结点
         * */
        public void delete(Node node){
            Node tmp=head;
            if(tmp.next==null){
                System.out.println("链表为空!");return;
            }
            while(true){
                if(tmp.next.no== node.no){
                    tmp.next=tmp.next.next;return;
                }
                tmp=tmp.next;
            }
        }
        /**显示链表*/
        public void list(){
            if(head.next==null){
                System.out.println("链表为空");return;
            }
            Node tmp=head.next;
            while(true){
                //判断是否到链表最后
                if(tmp==null){
                    return;
                }
                System.out.println(tmp);
                //将tmp后移
                tmp=tmp.next;
            }
        }
    }
    /**
     * 定义一个Node节点
     * */
    static class Node {
        public int no;
        public String name;
        public String nickname;
        public Node next;

        public Node(int no, String name, String nickname) {
            this.no = no;
            this.name = name;
            this.nickname = nickname;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "no=" + no +
                    ", name='" + name + '\'' +
                    ", nickname='" + nickname + '\'' +
                    '}';
        }
    }
}
