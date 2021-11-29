package tourGuide.service;

import tourGuide.model.Location;
import tourGuide.user.User;
import tourGuide.user.UserReward;

import java.util.List;

public interface IRewardsService {

    /**
     * This method is used to calculate the rewards for a targeted user
     *
     * @param user the targeted user
     */
    public void calculateRewards(User user);

    /**
     * This method is used to calculate the user rewards with better performance thanks to stream, threads, and ExecutorService
     *
     * @param users the list of the targeted users
     * @throws InterruptedException the exception thrown when a thread is waiting, sleeping, or otherwise occupied,
     *                              and the thread is interrupted, either before or during the activity.
     */
    public void calculateUsersRewards(List<User> users) throws InterruptedException;

    /**
     * This method adds a reward in the user rewards if this reward is not already in this list
     *
     * @param user       the targeted user
     * @param userReward the former list of user rewards
     * @return the updated list of the user rewards
     */
    public boolean addIfNotInUserRewards(User user, UserReward userReward);

    /**
     * This method calculates the distance between two locations
     *
     * @param loc1 the first location
     * @param loc2 the second location
     * @return the distance between the two locations
     */
    public double getDistance(Location loc1, Location loc2);

}
