package com.snca.financebidding;

public class AttachmentVO {
  private String desKey;

  private String digest;

  private String signature;

  private String bidderPublicKey;

  public String toString() {
    return "AttachmentVO [bidderPublicKey=" + bidderPublicKey + ", desKey=" + desKey + ", digest=" + digest
      + ", signature=" + signature + "]";
  }

  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((bidderPublicKey == null) ? 0 : bidderPublicKey.hashCode());
    result = prime * result + ((desKey == null) ? 0 : desKey.hashCode());
    result = prime * result + ((digest == null) ? 0 : digest.hashCode());
    result = prime * result + ((signature == null) ? 0 : signature.hashCode());
    return result;
  }

  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AttachmentVO other = (AttachmentVO) obj;
    if (bidderPublicKey == null) {
      if (other.bidderPublicKey != null)
        return false;
    } else if (!bidderPublicKey.equals(other.bidderPublicKey))
      return false;
    if (desKey == null) {
      if (other.desKey != null)
        return false;
    } else if (!desKey.equals(other.desKey))
      return false;
    if (digest == null) {
      if (other.digest != null)
        return false;
    } else if (!digest.equals(other.digest))
      return false;
    if (signature == null) {
      if (other.signature != null)
        return false;
    } else if (!signature.equals(other.signature))
      return false;
    return true;
  }

  public String getDesKey() {
    return desKey;
  }

  public void setDesKey(String desKey) {
    this.desKey = desKey;
  }

  public String getDigest() {
    return digest;
  }

  public void setDigest(String digest) {
    this.digest = digest;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getBidderPublicKey() {
    return bidderPublicKey;
  }

  public void setBidderPublicKey(String bidderPublicKey) {
    this.bidderPublicKey = bidderPublicKey;
  }
}
