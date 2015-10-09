import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class FuncionSucesora implements SuccessorFunction {

    private static final int N_OPERADORES = 5;  /*Placeholder number*/

    @Override
    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        Estado estado = (Estado) aState;

        for (int i = 0; i < N_OPERADORES ; ++i) {
            // TODO :preguntar operador y aplicarlo
        }
        return retVal;
    }
}
