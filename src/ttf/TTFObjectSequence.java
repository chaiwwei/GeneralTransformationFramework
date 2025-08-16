package ttf;

public class TTFObjectSequence {
    TTFObject head;
    TTFObject tail;
    public TTFObjectSequence(String content) {
        this.head = new TTFObject(null);
        this.tail = new TTFObject(null);
        this.head.next = this.tail;
        int len = content.length();
        TTFObject prev = this.head;
        for (int i = 0; i < len; i++) {
            TTFObject obj = new TTFObject(this.tail);
            prev.next = obj;
            prev = obj;
        }
        // TODO Auto-generated constructor stub
    }

    public int insert(int pos) {
        TTFObject prev = this.findPrevObjByPos(pos);
        TTFObject obj = new TTFObject(this.tail);

        obj.next = prev.next;
        prev.next = obj;

        return this.indexOf(obj);
    }

    public int delete (int pos) {
        TTFObject obj = this.findObjByPos(pos);
        if (obj.visible == false) {
            return -1;
        } else {
            obj.visible = false;
            return this.indexOf(obj);
        }
    }

    private TTFObject findPrevObjByPos(int pos) {
        if (pos == 0) {
            return this.head;
        }

        TTFObject obj = this.head.next;
        for (int num = 0; obj != this.tail; obj = obj.next) {
            if (obj.visible == true) {
                if (pos == num + 1) {
                    break;
                } else {
                    num = num + 1;
                }
            }
        }

        return obj;
    }

    private TTFObject findObjByPos(int pos) {
        TTFObject obj = this.head.next;
        for (int num = 0; obj != this.tail; obj = obj.next) {
            if (obj.visible == true) {
                if (pos == num) {
                    break;
                } else {
                    num = num + 1;
                }
            }
        }

        return obj;
    }

    private int indexOf(TTFObject target) {
        int num = 0;
        for (TTFObject obj = this.head.next; obj != target; obj = obj.next) {
            if (obj.visible == true) {
                num = num + 1;
            }
        }
        return num;
    }

}