package ecologylab.concurrent;

import ecologylab.net.ParsedURL;

/**
 * An abstraction of a site for accessing and downloading.
 * 
 * @author quyin
 */
public interface Site
{

  /**
   * @return The domain of this site.
   */
  String domain();

  /**
   * @return If this site should be ignored for accessing and downloading information.
   */
  boolean isIgnored();

  /**
   * Set if this site should be ignored.
   */
  void setIgnored(boolean ignored);

  /**
   * @return If the site is down.
   */
  boolean isDown();

  /**
   * @return If we are downloading information from this site rignt how.
   */
  boolean isDownloading();

  /**
   * @return The system time when last time we download information from this site.
   */
  long getLastDownloadAt();

  /**
   * @return If downloading information from this site is constrained by an interval.
   */
  boolean isDownloadingConstrained();

  /**
   * @return A decent interval between downloading from this site, in millisecond.
   */
  long getDecentDownloadInterval();

  /**
   * @return The next system time that this site can be downloaded.
   */
  long getNextAvailableTime();

  /**
   * Advance the internal record of the next system time that this site can be downloaded.
   */
  void advanceNextAvailableTime();

  /**
   * Set a really long time before this site can be downloaded again.
   */
  void setAbnormallyLongNextAvailableTime();

  // ****************************************************************
  // *                           Actions:                           *
  // ****************************************************************

  /**
   * Queue a location of this site for downloading. However, the downloading has not yet actually
   * happened.
   * 
   * @param location
   */
  void queueDownload(ParsedURL location);

  /**
   * Begin downloading information from this site with the given location.
   * 
   * @param location
   */
  void beginDownload(ParsedURL location);

  /**
   * End downloading information from this site with the given location. There could be downloading
   * ongoing for this site with other locations.
   * 
   * @param location
   */
  void endDownload(ParsedURL location);

  // ****************************************************************
  // *                          Counters:                           *
  // ****************************************************************

  void countNormalDownload(ParsedURL location);

  void countTimeout(ParsedURL location);

  void countFileNotFound(ParsedURL location);

  void countOtherIoError(ParsedURL location);

  int numOfNormalDownloads();

  int numOfTimeouts();

  int numOfNotFounds();

  int numOfOtherIoError();

}