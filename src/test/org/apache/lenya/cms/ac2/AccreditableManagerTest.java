/**
 * @author <a href="mailto:andreas@apache.org">Andreas Hartmann</a>
 */
package org.apache.lenya.cms.ac2;

import junit.textui.TestRunner;

import org.apache.avalon.framework.component.ComponentSelector;
import org.apache.lenya.cms.ExcaliburTest;
import org.apache.lenya.cms.PublicationHelper;
import org.apache.lenya.cms.ac.AccessControlException;

/**
 * @author andreas
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AccreditableManagerTest extends ExcaliburTest {

    /**
     * @param test The test to execute.
     */
    public AccreditableManagerTest(String test) {
        super(test);
    }

    /**
     * The main program.
     * The parameters are set from the command line arguments.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        args = PublicationHelper.extractPublicationArguments(args);
        TestRunner.run(AccreditableManagerTest.class);
    }
    
    private AccreditableManager accreditableManager;
    private ComponentSelector selector;
    
    protected static final String HINT = "file";

    /**
     * The JUnit setup method. Lookup the resolver role.
     *
     * @exception  Exception  Description of Exception
     * @since
     */
    public void setUp() throws Exception {
        super.setUp();

        String role = AccreditableManager.ROLE + "Selector";
        selector = (ComponentSelector) manager.lookup(role);
        
        accreditableManager = (AccreditableManager) selector.select(HINT);
        assertNotNull("AccreditableManager is null", accreditableManager);
    }

    /**
     * The test.
     * @throws AccessControlException when something went wrong.
     */
    public void testAccreditableManager() throws AccessControlException {
        assertNotNull(accreditableManager.getUserManager());
        assertNotNull(accreditableManager.getGroupManager());
        assertNotNull(accreditableManager.getRoleManager());
        
    }

}
