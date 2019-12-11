from app_faust import app
from .model import AdCampaignClickInfo
from .model import AdCampaignStatistics
from faust.types import StreamT
from .ad_data_tables import ad_info

campaign = app.topic("ad_campaign", value_type=AdCampaignClickInfo)
campaign_statistics = app.topic("ad_campaign_statistics", value_type=AdCampaignStatistics)



@app.agent(campaign, sink=[campaign_statistics])
async def count_occurrence(stream_campaign: StreamT[str]):
    async for click in stream_campaign.group_by(AdCampaignClickInfo.camp_id):
        if click.camp_id not in ad_info:
            ad_info[click.camp_id] = build_analytics_object(click.camp_id)

        persistant_ad = ad_info[click.camp_id]

        persistant_ad.total += 1
        if click.is_fake == 1:
            persistant_ad.fake_clicks += 1
        elif click.is_fake == 0:
            persistant_ad.valid_clicks += 1

        persistant_ad = calc_fake_and_valid_percent(persistant_ad)

        ad_info[click.camp_id] = persistant_ad

        yield persistant_ad


def build_analytics_object(campaign_id) -> AdCampaignStatistics:
    info = {
        "camp_id": campaign_id,
        "fake_clicks": 0,
        "valid_clicks": 0,
        "total": 0,
        "fraud_percent": 0.0,
        "valid_percent": 0.0
    }
    return AdCampaignStatistics(**info)


def calc_fake_and_valid_percent(ad_object):
    ad_object.fraud_percent = round(ad_object.fake_clicks / ad_object.total * 100, 2)
    ad_object.valid_percent = round(ad_object.valid_clicks / ad_object.total * 100, 2)
    return ad_object
