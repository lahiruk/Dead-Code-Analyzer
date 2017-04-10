package lk.devfactory.repository.impl;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringRunner;

import lk.devfactory.SystemPropertyTestSupport;
import lk.devfactory.model.DeadCode;
import lk.devfactory.model.Function;
import lk.devfactory.model.FunctionParameter;
import lk.devfactory.model.GlobalVariable;
import lk.devfactory.model.LocalVariable;
import lk.devfactory.reposiotry.DeadCodeDO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnderstandDeadCodeDOTests extends SystemPropertyTestSupport {

	Repository repository;
	UUID uuid;
	List<DeadCode> deadCodeList;

	@Autowired
	DeadCodeDO deadCodeDO; // SUT

	@Before
	public void before() {
		deadCodeList = new ArrayList<DeadCode>();
	}

	@Test
	public void analysis() {
		deadCodeList = deadCodeDO.analyse("ead82e91-846a-4f65-95ce-e480639a5838");
		// TODO add asserts
		deadCodeList.forEach(dead -> {
			//System.out.println("Dead code:");
			//System.out.println(dead);
			assertEquals("lk.Application", dead.toString());

			// Compare without order check. Order irrelevant
			List<GlobalVariable> expGV = new ArrayList<GlobalVariable>();
			expGV.add(new GlobalVariable("Application.unusedGlobal", 0, 0));
			expGV.add(new GlobalVariable("Application.publicGlobal", 0, 0));
			expGV.add(new GlobalVariable("Application.protectedGlobal", 0, 0));

			assertEquals(new HashSet<GlobalVariable>(expGV), new HashSet<GlobalVariable>(dead.getGlobalVariables()));

			dead.getGlobalVariables().forEach(gv -> {
				//System.out.println("Global Var:");
				//System.out.println(gv + "[" + gv.getLineNo() + "," + gv.getColumnNo() + "]");
				assertTrue("lineNo not greater than 0 for "+gv, gv.getLineNo() > 0);
				assertTrue("columnNonot greater than 0 for "+gv, gv.getColumnNo() > 0);
			});

			List<Function> expFun = new ArrayList<Function>();
			expFun.add(new Function("Application.main", 0, 0, null, null));
			expFun.add(new Function("Application.unusedFunction", 0, 0, null, null));
			expFun.add(new Function("Application.usedFunction", 0, 0, null, null));
			expFun.add(new Function("Application.protectedFunction", 0, 0, null, null));
			expFun.add(new Function("Application.returnFunction", 0, 0, null, null));

			assertEquals(new HashSet<Function>(expFun), new HashSet<Function>(dead.getFunctions()));

			dead.getFunctions().forEach(fun -> {
				//System.out.println("Function:");
				//System.out.println(fun + "[" + fun.getLineNo() + "," + fun.getColumnNo() + "]");
				if (!"Application.usedFunction".equals(fun.toString())) {
					assertTrue("lineNo not greater than 0 for "+fun, fun.getLineNo() > 0);
					assertTrue("columnNonot greater than 0 for "+fun, fun.getColumnNo() > 0);
				}

				//System.out.println("Function Parameter:");
				//System.out.println(fun.getParameters());
				//System.out.println("Local var:");
				//System.out.println(fun.getVariables());

				if ("Application.unusedFunction".equals(fun.toString())) {
					List<FunctionParameter> expFunParam = new ArrayList<FunctionParameter>();
					expFunParam.add(new FunctionParameter("unusedParam", 0, 0));
					assertEquals(new HashSet<FunctionParameter>(expFunParam),
							new HashSet<FunctionParameter>(fun.getParameters()));

					fun.getParameters().forEach(param -> {
						//System.out.println(param + "[" + param.getLineNo() + "," + param.getColumnNo() + "]");
						
						assertTrue("lineNo not greater than 0 for "+param, param.getLineNo() > 0);
						assertTrue("columnNonot greater than 0 for "+param, param.getColumnNo() > 0);
					});

					List<LocalVariable> expFunVar = new ArrayList<LocalVariable>();
					expFunVar.add(new LocalVariable("unusedVariable", 0, 0));
					assertEquals(new HashSet<LocalVariable>(expFunVar), new HashSet<LocalVariable>(fun.getVariables()));

					fun.getVariables().forEach(var -> {
						//System.out.println(var + "[" + var.getLineNo() + "," + var.getColumnNo() + "]");
						
						assertTrue("lineNo not greater than 0 for "+var, var.getLineNo() > 0);
						assertTrue("columnNonot greater than 0 for "+var, var.getColumnNo() > 0);
					});
				} else if ("Application.usedFunction".equals(fun.toString())) { 
					fun.getParameters().forEach(param -> {
						//System.out.println(param + "[" + param.getLineNo() + "," + param.getColumnNo() + "]");
						
						assertTrue("lineNo not greater than 0 for "+param, param.getLineNo() > 0);
						assertTrue("columnNonot greater than 0 for "+param, param.getColumnNo() > 0);
					});
				} else {
					assertTrue("The function " + fun.getName() + " should not have any parameters but has "
							+ fun.getParameters().size(), fun.getParameters().size() == 0);
					assertTrue("The function " + fun.getName() + " should not have any local variables but has "
							+ fun.getVariables().size(), fun.getVariables().size() == 0);
				}
			});
		});
	}
}
