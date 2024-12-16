package models;

public class WishList {

	public class Wishlist {
		private String wishlistId;
	    private String userId;
	    private String ItemId;


	    public Wishlist(String wishlistId, String userId, String itemId) {
	        this.wishlistId = wishlistId;
	        this.userId = userId;
	        this.ItemId = itemId;
	    }


		public String getWishlistId() {
			return wishlistId;
		}


		public void setWishlistId(String wishlistId) {
			this.wishlistId = wishlistId;
		}


		public String getUserId() {
			return userId;
		}


		public void setUserId(String userId) {
			this.userId = userId;
		}


		public String getItemId() {
			return ItemId;
		}


		public void setItemId(String ItemId) {
			this.ItemId = ItemId;
		}

	    
	    
	    

	}

}
