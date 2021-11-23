package tourGuide.helper;

/**
 * This class is used to set the number of users in tests
 */
public class InternalTestHelper {

    // Set this default up to 100,000 for testing
    private static int internalUserNumber = 10;

    public static void setInternalUserNumber(int internalUserNumber) {
        InternalTestHelper.internalUserNumber = internalUserNumber;
    }

    public static int getInternalUserNumber() {
        return internalUserNumber;
    }
}
