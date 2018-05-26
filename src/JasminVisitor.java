import java.util.LinkedList;

public class JasminVisitor implements ParserVisitor {

    private JasminGenerator jasminGenerator;

    public JasminVisitor(JasminGenerator jasminGenerator) {
        this.jasminGenerator = jasminGenerator;
    }

    public Object visit(SimpleNode node, Object data) {
        return null;
    }

    public Object visit(ASTerror_skipto node, Object data) {
        return null;
    }

    public Object visit(ASTerror_skipto_andEat node, Object data) {
        return null;
    }

    public Object visit(ASTStart node, Object data) {
        node.jjtGetChild(0).jjtAccept(this, null);
        return null;
    }

    public Object visit(ASTModule node, Object data) {
        String moduleName = (String) node.jjtGetValue();

        this.jasminGenerator.writeModule(moduleName);

        LinkedList<Element> elements = this.jasminGenerator.getRootSymbolTable().getElements();

        int fields = this.jasminGenerator.writeFields(elements);

        if( fields > 0)
        {
            this.jasminGenerator.writeInitMethod();
        }
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
            if (i == fields-1) {
                this.jasminGenerator.writeEndMethod();
            }
        }

        return null;
    }

    public Object visit(ASTDeclaration node, Object data) {
        Element leftNode = (Element) node.jjtGetChild(0).jjtAccept(this, true);
        if(leftNode.getType() != Type.ARRAY)
            return null;
        node.jjtGetChild(1).jjtAccept(this, false);
        this.jasminGenerator.writePutstatic(leftNode);
        return null;
    }

    public Object visit(ASTScalarDeclaration node, Object data) {
        return null;
    }

    public Object visit(ASTSign node, Object data) {
        return null;
    }

    public Object visit(ASTScalar node, Object data) {
        this.jasminGenerator.writeScalar((String)node.jjtGetValue());
        return null;
    }

    public Object visit(ASTArrayDeclaration node, Object data) {
        node.jjtGetChild(0).jjtAccept(this,false);
        this.jasminGenerator.writeArray();
        return null;
    }

    public Object visit(ASTFunction node, Object data) {
        SymbolTable currentSymbolTable = this.jasminGenerator.getCurrentSymbolTable();
        this.jasminGenerator.pushFront(currentSymbolTable.popChild());
        this.jasminGenerator.writeBeginMethod(this.jasminGenerator.getCurrentSymbolTable());
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        this.jasminGenerator.writeEndMethod();
        this.jasminGenerator.popFront();
        return null;
    }

    public Object visit(ASTReturn node, Object data) {
        return null;
    }

    public Object visit(ASTParameters node, Object data) {
        return null;
    }

    public Object visit(ASTVariable node, Object data) {
        Element element = this.jasminGenerator.getCurrentSymbolTable().getElement((String)node.value);

        if((boolean) data)
            return element;

        this.jasminGenerator.writeLoadElement(element);
        return null;
    }

    public Object visit(ASTArrayElement node, Object data) {
        return null;
    }

    public Object visit(ASTStatements node, Object data) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            node.jjtGetChild(i).jjtAccept(this, data);
        }
        return null;
    }

    public Object visit(ASTAssign node, Object data) {
        return null;
    }

    public Object visit(ASTOperation node, Object data) {
        return null;
    }

    public Object visit(ASTAccess node, Object data) {
        SymbolTable currentSymbolTable = this.jasminGenerator.getCurrentSymbolTable();
        Element element = currentSymbolTable.getElement((String) node.value);

        if((boolean) data){
            return element;
        }

        this.jasminGenerator.writeLoadElement(element);

        if(element.getType() != Type.ARRAY)
            return null;

        Object object = node.jjtGetChild(0).jjtAccept(this, false);
        if(object!= null){
            if(object instanceof Boolean){
                if(!(Boolean)object)
                    return null;
            }
        }

        this.jasminGenerator.writeIaload();

        return null;
    }

    public Object visit(ASTTerm node, Object data) {
        return null;
    }

    public Object visit(ASTCall node, Object data) {
        return null;
    }

    public Object visit(ASTFunctionName node, Object data) {
        return null;
    }

    public Object visit(ASTSize node, Object data) {
        this.jasminGenerator.writeArraySize();

        return Boolean.FALSE;
    }

    public Object visit(ASTConditionalOperation node, Object data) {
        return null;
    }

    public Object visit(ASTWhile node, Object data) {

        ASTConditionalOperation conditionNode = (ASTConditionalOperation) node.jjtGetChild(0);
        ASTStatements statementNode = (ASTStatements) node.jjtGetChild(1);

        this.jasminGenerator.writeWhile(conditionNode, statementNode, this);

        return null;
    }

    public Object visit(ASTIf node, Object data) {
        return null;
    }

    public Object visit(ASTArgumentList node, Object data) {
        return null;
    }

    public Object visit(ASTString node, Object data) {
        return null;
    }
}