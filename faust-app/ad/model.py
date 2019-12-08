import faust


class AdCampaignClickInfo(faust.Record):
    cookie: str
    camp_id: str
    is_fake: int



class AdCampaignStatistics(faust.Record):
    camp_id: str
    fake_clicks: str
    valid_clicks: int
    total: int
    fraud_percent: float
    valid_percent: float

