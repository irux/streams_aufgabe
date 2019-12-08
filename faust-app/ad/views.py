from app_faust import app
from .ad_data_tables import ad_info


@app.page("/campaign/stats/")
async def show_stats(self, request):
    campaigns = [campaign for key, campaign in ad_info.items()]

    return self.json({
        "campaigns": campaigns
    })

@app.page("/campaign/stats/{camp_id}")
@app.table_route(table=ad_info, match_info='camp_id')
async def show_stats(self, request, camp_id):
    return self.json(ad_info[camp_id])








