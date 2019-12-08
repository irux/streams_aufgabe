from app_faust import app
from .model import AdCampaignStatistics

ad_info = app.Table("ad_info_table", default=AdCampaignStatistics)