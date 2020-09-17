package Database;

public class InterestData {
    private String interest;
    private String interestMetadata;

    public InterestData(String interest, String interestMetadata) {
        this.interest = interest;
        this.interestMetadata = interestMetadata;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getInterestMetadata() {
        return interestMetadata;
    }

    public void setInterestMetadata(String interestMetadata) {
        this.interestMetadata = interestMetadata;
    }
}
