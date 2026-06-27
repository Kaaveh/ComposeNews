package ir.composenews.core_test.repository;

import android.os.Build;

import androidx.paging.PagingData;

import java.util.Collections;
import java.util.List;

import ir.composenews.domain.model.Market;
import ir.composenews.domain.model.MarketChart;
import ir.composenews.domain.model.MarketDetail;
import ir.composenews.domain.repository.MarketRepository;
import ir.composenews.network.Errors;
import ir.composenews.network.Resource;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.collections.immutable.ExtensionsKt;
import kotlinx.coroutines.flow.Flow;
import kotlinx.coroutines.flow.FlowKt;

public final class FakeMarketRepository implements MarketRepository {
    public static final long START = 0L;
    public static final double END = 100000.0;

    private final List<Market> markets = Collections.singletonList(
            new Market(
                    "bitcoin",
                    "Bitcoin",
                    "btc",
                    100000.0,
                    2.5,
                    "",
                    false
            )
    );

    @Override
    public Flow<List<Market>> getMarketList() {
        return FlowKt.flowOf(markets);
    }

    @Override
    public Flow<PagingData<Market>> getPagedMarketList() {
        return FlowKt.flowOf(PagingData.from(markets));
    }

    @Override
    public Flow<List<Market>> getFavoriteMarketList() {
        return FlowKt.flowOf(Collections.emptyList());
    }

    @Override
    public Flow<Market> getMarketById(String id) {
        Market market = null;
        // Since this is a fake repository and we just use it inside tests, this code will be fine
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            market = markets.stream()
                    .filter(item -> item.getId().equals(id))
                    .findFirst()
                    .orElse(null);
        }
        return FlowKt.flowOf(market);
    }

    @Override
    public Object syncMarketList(Continuation<? super Unit> continuation) {
        return Unit.INSTANCE;
    }

    @Override
    public Object toggleFavoriteMarket(
            Market oldMarket,
            Continuation<? super Unit> continuation
    ) {
        return Unit.INSTANCE;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Flow<Resource<MarketChart, Errors>> fetchChart(String id) {
        MarketChart chart = new MarketChart(
                ExtensionsKt.persistentListOf(new Pair<>(START, END))
        );
        return FlowKt.flowOf(success(chart));
    }

    @Override
    public Flow<Resource<MarketDetail, Errors>> fetchDetail(String id) {
        MarketDetail.MarketData marketData = new MarketDetail.MarketData(
                101000.0,
                99000.0,
                1_000_000_000L,
                1
        );
        MarketDetail detail = new MarketDetail(id, 1, marketData, "Bitcoin");
        return FlowKt.flowOf(success(detail));
    }

    @SuppressWarnings("unchecked")
    private static <T> Resource<T, Errors> success(T data) {
        return (Resource<T, Errors>) (Resource<?, ?>) new Resource.Success<>(data);
    }
}
