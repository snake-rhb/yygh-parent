import com.nsu.yygh.model.cmn.Dict;

public class DictTest {
    public static void main(String[] args) {
        Dict d1 = new Dict();
        d1.setId(1L);
        Dict d2 = new Dict();
        d2.setId(2L);

        System.out.println(d1.equals(d2));
    }
}
